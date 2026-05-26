#!/bin/bash
# PostToolUse hook: detects drift between *Contract.kt and its spec YAML.

input=$(cat)
file_path=$(echo "$input" | python3 -c "
import json, sys
d = json.load(sys.stdin)
print(d.get('tool_input', {}).get('file_path', ''))
" 2>/dev/null)

[[ "$file_path" == *"Contract.kt" ]] || exit 0

filename=$(basename "$file_path" .kt)
feature="${filename%Contract}"

# PascalCase → snake_case
snake=$(echo "$feature" \
  | sed 's/\([A-Z]\)/_\1/g' \
  | sed 's/^_//' \
  | tr '[:upper:]' '[:lower:]')

repo_root=$(git rev-parse --show-toplevel 2>/dev/null)
[[ -n "$repo_root" ]] || { echo "⚠️  Cannot locate repo root — skipping drift check."; exit 0; }

spec=$(find "$repo_root/specs" -name "${snake}.yaml" 2>/dev/null | head -1)
[[ -n "$spec" ]] || { echo "⚠️  No spec found for $filename — create specs/feature/.../${snake}.yaml"; exit 0; }

python3 - "$spec" "$file_path" <<'PYEOF'
import sys, re

spec_path, contract_path = sys.argv[1], sys.argv[2]

with open(spec_path) as f:
    spec = f.read()
with open(contract_path) as f:
    contract = f.read()

# Spec: state field names (under "  fields:" block, before next top-level key)
state_sec = re.search(r'^\s{0,2}fields:\s*\n(.*?)(?=^\S|\Z)', spec, re.MULTILINE | re.DOTALL)
spec_fields = set(re.findall(r'^\s+-\s+name:\s+(\w+)', state_sec.group(1) if state_sec else '', re.MULTILINE))

# Spec: intent names (under "intents:" block, before next top-level key)
intents_sec = re.search(r'^intents:\s*\n(.*?)(?=^\S|\Z)', spec, re.MULTILINE | re.DOTALL)
spec_intents = set(re.findall(r'^\s+-\s+name:\s+(\w+)', intents_sec.group(1) if intents_sec else '', re.MULTILINE))

# Contract: UiState constructor parameter names
uistate_sec = re.search(r'data class \w*UiState\s*\((.*?)\)', contract, re.DOTALL)
contract_fields = set(re.findall(r'(?:val|var)\s+(\w+)\s*:', uistate_sec.group(1) if uistate_sec else ''))

# Contract: Intent subclass names (single-line ": Intent()" declarations)
contract_intents = set(re.findall(r'(?:data class|object)\s+(\w+)[^\n]*:\s*Intent\(\)', contract))

drifts = []
for f in sorted(spec_fields - contract_fields):
    drifts.append(f"  FIELD in spec, missing in UiState:   {f}")
for f in sorted(contract_fields - spec_fields):
    drifts.append(f"  FIELD in UiState, missing in spec:   {f}")
for i in sorted(spec_intents - contract_intents):
    drifts.append(f"  INTENT in spec, missing in Contract: {i}")
for i in sorted(contract_intents - spec_intents):
    drifts.append(f"  INTENT in Contract, missing in spec: {i}")

name = contract_path.split('/')[-1]
if drifts:
    print(f"⚠️  Spec drift detected in {name}:")
    for d in drifts:
        print(d)
    print(f"   Spec: {spec_path}")
else:
    print(f"✓  {name} matches spec.")
PYEOF