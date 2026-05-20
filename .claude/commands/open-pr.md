Open a pull request for the current branch following the project's GitFlow conventions.

## Steps

### 1. Read context

Run these in parallel:
- `git branch --show-current` — get current branch name
- `git log main..HEAD --oneline` — list commits in this branch
- `git diff $(git merge-base HEAD develop 2>/dev/null || git merge-base HEAD main)..HEAD --stat` — changed files summary

### 2. Determine PR metadata from branch name

**Target branch (GitFlow):**
| Branch prefix | Target |
|---|---|
| `feature/*` | `develop` |
| `fix/*` or `bugfix/*` | `develop` |
| `hotfix/*` | `main` |
| `release/*` | `main` |
| `chore/*` or `docs/*` or anything else | `develop` |

**Label:**
| Branch prefix | Label |
|---|---|
| `feature/*` | `enhancement` |
| `fix/*` / `bugfix/*` / `hotfix/*` | `bug` |
| `docs/*` | `documentation` |
| `chore/*` or other | _(no label)_ |

**Issue number:** extract from branch name if present (e.g. `feature/65-add-home-screen` → issue `65`).

**PR type:** `feature` if `feature/*`, else `fix` if `fix/*`/`bugfix/*`/`hotfix/*`, else `other`.

### 3. Push branch if not already pushed

```bash
git push -u origin <current-branch>
```

### 4. Choose and fill PR template

**Feature template:**
```
## Summary
<!-- What this feature does and why -->
- <bullet 1>
- <bullet 2>

## Changes
<!-- Key files / modules touched -->
- `module:path` — what changed

## Test plan
- [ ] <golden path scenario>
- [ ] <edge case>

## Linked issue
Closes #<issue-number>  ← only if issue number found

🤖 Generated with [Claude Code](https://claude.com/claude-code)
```

**Fix template:**
```
## What was broken
<!-- Symptom visible to users or tests -->

## Root cause
<!-- Why it broke -->

## Fix
<!-- What changed to resolve it -->

## Test plan
- [ ] <repro steps that now pass>
- [ ] <regression check>

## Linked issue
Closes #<issue-number>  ← only if issue number found

🤖 Generated with [Claude Code](https://claude.com/claude-code)
```

**Other / chore / docs template:**
```
## Summary
<!-- What and why -->
- <bullet>

## Changes
- `path` — what changed

🤖 Generated with [Claude Code](https://claude.com/claude-code)
```

Fill the body by analysing the commits and diff stat gathered in step 1. Do not write placeholder text — fill real content.

### 5. Create the PR

```bash
gh pr create \
  --title "<conventional-commit title derived from commits>" \
  --base <target-branch> \
  --assignee rbrthmn \
  --label <label>  \   ← omit if no label applies
  --body "$(cat <<'EOF'
<filled template body>
EOF
)"
```

Title format: `<type>(<scope>): <short description>` — derive from the dominant commit type on this branch.

### 6. Link PR to GitHub Project and set status to Code Review

After `gh pr create` returns the PR URL, extract the PR number from it, then:

```bash
# Add PR to project
PR_URL=<url from gh pr create>
gh project item-add 4 --owner "@me" --url "$PR_URL"

# Get item ID for this PR
ITEM_ID=$(gh project item-list 4 --owner "@me" --format json | python3 -c "
import json, sys
items = json.load(sys.stdin)['items']
for i in items:
    c = i.get('content', {})
    if c.get('url') == '$PR_URL' or str(c.get('number','')) in '$PR_URL':
        print(i['id'])
        break
")

# Set status to Code Review
gh project item-edit \
  --project-id PVT_kwHOAo5Oes4Akxw8 \
  --id "$ITEM_ID" \
  --field-id PVTSSF_lAHOAo5Oes4Akxw8zgc7r2s \
  --single-select-option-id 5f0eb544
```

If a matching project item already exists (from an issue linked to this branch), update that item instead of adding a new one.

### 7. Report back

Print:
- PR URL
- Target branch
- Label applied
- Project item status set to "Code Review"