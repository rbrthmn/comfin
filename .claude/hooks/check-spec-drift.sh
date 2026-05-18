#!/bin/bash
# PostToolUse hook: warns when a *Contract.kt is edited without updating its spec.
# Receives JSON tool-use payload on stdin.

input=$(cat)
file_path=$(echo "$input" | python3 -c "
import json, sys
d = json.load(sys.stdin)
print(d.get('tool_input', {}).get('file_path', ''))
" 2>/dev/null)

[[ "$file_path" == *"Contract.kt" ]] || exit 0

filename=$(basename "$file_path" .kt)   # e.g. BalanceCardContract
feature="${filename%Contract}"           # e.g. BalanceCard

# PascalCase → snake_case
snake=$(echo "$feature" \
  | sed 's/\([A-Z]\)/_\1/g' \
  | sed 's/^_//' \
  | tr '[:upper:]' '[:lower:]')         # e.g. balance_card

spec=$(find specs/ -name "${snake}.yaml" 2>/dev/null | head -1)

if [ -n "$spec" ]; then
  echo "⚠️  Spec drift check: $(basename "$file_path") was modified."
  echo "   Verify spec is still accurate: $spec"
fi