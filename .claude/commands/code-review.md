Review the current branch's changes against the PR description. Focus on new/changed/deleted lines only — not pre-existing code.

## Steps

### 1. Gather context (run all in parallel)

```bash
# Current branch
git branch --show-current

# PR description and title
gh pr view --json title,body,number 2>/dev/null || echo "NO_PR"

# Changed files list
git diff $(git merge-base HEAD develop 2>/dev/null || git merge-base HEAD main)..HEAD --name-only

# Full diff (new/changed/deleted lines)
git diff $(git merge-base HEAD develop 2>/dev/null || git merge-base HEAD main)..HEAD

# Commits on this branch
git log $(git merge-base HEAD develop 2>/dev/null || git merge-base HEAD main)..HEAD --oneline
```

### 2. Read changed files

For each `.kt` file in the changed list, read the full file — you need full context to judge dead code and architecture violations, even though you only **report** on changed lines.

### 3. Run the review

Apply every lens below. For each finding, cite the exact file and line number from the diff. Skip lenses that don't apply (e.g. no new public API → skip dead-method check).

---

#### Lens A — Architecture (MVVM + MVI + Contract pattern)

Rules from this project:

- Every screen has `*Contract.kt` with `UiState` (immutable data class), `Intent` (sealed class), `Effect` (sealed class), and abstract `ViewModel`.
- `UiState` must be immutable — only `copy()` for updates. No `var` fields.
- `Intent` represents user actions only. No business logic inside Intent classes.
- `Effect` for one-time events (navigation, toasts). Never put navigation directly in ViewModel logic outside of Effect.
- ViewModels extend `BaseViewModel<U, I>`. Must implement `uiState: StateFlow<U>`, `onIntent(intent: I)`, `doOnInit()`.
- Internal state via `MutableStateFlow`, exposed as read-only `StateFlow`.
- No business logic in Composables — only UI rendering and intent dispatch.
- Feature modules depend only on their corresponding `data:X` module. No cross-feature dependencies.
- DI via Koin. Each feature has `di/{Feature}Module.kt`.

Flag: missing Contract, mutable UiState fields, logic in Composables, cross-feature imports, ViewModel not extending BaseViewModel.

---

#### Lens B — SOLID

- **S** — Single Responsibility: class/function does one thing. Flag god classes, functions doing multiple unrelated things.
- **O** — Open/Closed: new behavior via extension, not modification of existing sealed/final structures without reason.
- **L** — Liskov: subtypes behave correctly. Flag overrides that weaken postconditions or throw unexpectedly.
- **I** — Interface Segregation: no fat interfaces forcing empty implementations.
- **D** — Dependency Inversion: depend on abstractions (repository interfaces, not Room DAOs directly in ViewModels).

---

#### Lens C — Clean Code

- Names reveal intent — no single-letter vars outside tiny lambdas, no abbreviations that obscure meaning.
- Functions do one thing, are short (flag functions over ~30 lines unless unavoidable).
- No magic numbers or hardcoded strings in Composables — must use `R.string`, `R.dimen`, etc.
- No unnecessary comments (code should be self-explanatory). Comments only for non-obvious WHY.
- No deeply nested logic (flag more than 3 levels of nesting).
- No swallowed exceptions or empty catch blocks.
- No `TODO`/`FIXME` left in changed lines unless explicitly discussed in PR description.

---

#### Lens D — Dead Code

Scan the full content of changed files for:

- Private functions never called within the file.
- Private/internal properties never read.
- `sealed class` branches / `when` cases that are unreachable.
- Imports that are unused.
- Parameters passed but never used inside the function body.
- Variables assigned but never read.

Report only items introduced or made dead by *this branch's changes* — not pre-existing dead code.

---

#### Lens E — Scope Fit (PR objective vs implementation)

Compare the PR title + body (from step 1) against what was actually implemented.

Ask:
- Does every changed file/class serve the stated PR objective?
- Are there abstractions, interfaces, or base classes added that have only one implementation and no stated plan for more?
- Are there generic utilities introduced for a single use case?
- Are there patterns applied (e.g. Strategy, Factory) where a simple function or `when` would do?
- Were files changed that are not mentioned in the PR scope and have no obvious connection?

Flag: over-engineering, unnecessary abstraction, scope creep, and also the inverse — PR description promising something not implemented.

---

#### Lens F — Spec Drift

For each changed `*Contract.kt` or `*ViewModel.kt`, find the corresponding spec YAML in `specs/feature/{module}/`.

Check:
- Every field in `UiState` maps to a field declared in the spec `state` section. Flag fields added to Contract not in spec.
- Every `Intent` subclass maps to an entry in the spec `intents` section. Flag intents not in spec.
- Every behavior `description` in the spec has a matching test method name (verbatim) in `*ViewModelTest.kt`. Flag behaviors with no test.
- No spec fields/intents silently removed from Contract without spec update.

If no spec file exists for a changed Contract, flag it: spec is missing, drift cannot be verified.

---

#### Lens G — Compose Performance

- Lambda passed as parameter not wrapped in `remember` or `rememberUpdatedState` → new function ref each recomposition → unnecessary child recompose. Flag inline lambdas in composable call sites.
- `data class` used in UiState or passed as composable param containing unstable types (plain `List`, `Map`, interfaces) without `@Stable` or `@Immutable` annotation → Compose cannot skip recomposition.
- Heavy computation (sorting, filtering, mapping collections) inside composable body instead of `remember(key) { }` or `derivedStateOf { }`.
- `LazyColumn` / `LazyRow` items missing `key` parameter → full list recompose on any data change.
- `collectAsState()` used instead of `collectAsStateWithLifecycle()` → flow active in background.

---

#### Lens H — Coroutine Safety

- `GlobalScope.launch` or `GlobalScope.async` in production code → unstructured, leaks.
- `runBlocking` outside of test code → blocks thread, defeats coroutine purpose.
- `viewModelScope.launch { }` with no `try/catch` or `CoroutineExceptionHandler` around suspending calls that can throw → silent crash.
- IO work (Room queries, file access, network) called on default dispatcher — must use `withContext(Dispatchers.IO)`.
- Flow collected via `collectAsState()` directly in a Composable without `collectAsStateWithLifecycle()` → subscription survives background, drains battery.
- `launch` used where `async/await` is needed for result, or vice versa — misuse of structured concurrency primitives.

---

### 4. Format output

Use the caveman-review style: one finding per line, no padding.

```
[FILE:LINE] LENS — problem. fix.
```

Group by lens. Use headers:

```
## A — Architecture
## B — SOLID
## C — Clean Code
## D — Dead Code
## E — Scope Fit
## F — Spec Drift
## G — Compose Performance
## H — Coroutine Safety
```

If a lens has zero findings, write: `A — no issues.`

End with a one-line **verdict**:
```
VERDICT: [APPROVE | REQUEST CHANGES | NEEDS DISCUSSION] — <reason in ≤15 words>
```

- **APPROVE** — only cosmetic or no findings
- **NEEDS DISCUSSION** — scope fit concerns or architectural trade-offs without a clear right answer
- **REQUEST CHANGES** — concrete violations found (architecture broken, dead code, SOLID violation)