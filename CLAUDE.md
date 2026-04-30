# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./gradlew assembleDebug
./gradlew assembleRelease

# Test
./gradlew testDebugUnitTest          # Unit tests
./gradlew connectedAndroidTest       # UI tests (requires device/emulator)

# Single module test
./gradlew :feature:home:testDebugUnitTest
```

## Architecture

Android personal finance app. Kotlin + Jetpack Compose. MVVM + MVI. Multi-module Gradle project.

**Module layout:**
- `:app` — Application entry, Koin setup, NavGraph, MainActivity
- `:core:ui` — `BaseViewModel`, shared composables, theme, utils
- `:core:navigation` — `NavigationDestination`, `ComFinNavigationBar`
- `:core:test` — `BaseUITest`, `ComposeRuleExtensions`
- `:data:common` — Room DB, all entities, all DAOs, TypeConverters, Koin DAO bindings
- `:data:finance` — BankAccount, CreditCard, Transaction models/mappers/repos
- `:data:wealth` — Reserve, ReserveTransaction, Investment models/mappers/repos
- `:data:budget` — Allocation, RecurringExpense models/mappers/repos
- `:feature:home` — Balance card, credit card bills, monthly limit widgets
- `:feature:operations` — Transaction management
- `:feature:settings` — App settings
- `:feature:misc` — Income divisions, more features screen

**Data flow:** User action → `onIntent()` → ViewModel → `MutableStateFlow` update → UI `collectAsState()` recompose

## Contract Pattern

Every screen needs a `*Contract.kt` defining three types and one abstract class:

```kotlin
object HomeScreenContract {
    data class UiState(...)           // immutable, single source of truth
    sealed class Intent { ... }       // user actions
    sealed class Effect { ... }       // one-time events (navigation, toasts)
    abstract class ViewModel : BaseViewModel<UiState, Intent>()
}
```

## ViewModel Rules

All ViewModels extend `br.com.rbrthmn.ui.BaseViewModel<U, I>` and must implement:
- `abstract val uiState: StateFlow<U>` — expose as read-only
- `abstract fun onIntent(intent: I)` — handle intents
- `abstract fun doOnInit(): BaseViewModel<U, I>` — initialization, returns `this`

Use `viewModelScope` for coroutines. Internal state via `MutableStateFlow`, exposed as `StateFlow`.

## DI (Koin)

Each feature module has a `di/{Feature}Module.kt`. Modules collected in `ComFin` Application class.

Inject in Compose: `koinViewModel()` / `koinInject()`.

## Navigation

Destinations implement `NavigationDestination` (defines `route: String`). Routes registered in `ComFinNavGraph.kt`. Bottom nav has 3 roots: Home, Operations, More Features.

## Testing

**Unit tests** — JUnit + Mockk. Name pattern: `{method} with {conditions} should {result}`. AAA body.

**UI tests** — Extend `BaseUITest`. Use `onNodeWithStringId()` helper. Tag composables with `testTag(SOME_TEST_TAG)`. Name pattern: `{method}_with{conditions}_should{result}`.

No new library dependencies without explicit instruction.

## GitHub Projects Workflow

Project: **PVT_kwHOAo5Oes4Akxw8** (number 4, owner `@me`, repo `rbrthmn/comfin`)

Status field ID: `PVTSSF_lAHOAo5Oes4Akxw8zgc7r2s`
Status option IDs: `f75ad846` = Todo · `47fc9ee4` = In Progress · `5f0eb544` = Code Review · `98236657` = Done

### Before starting any feature

1. Search existing cards:
```bash
gh project item-list 4 --owner "@me" --format json | python3 -c "import json,sys; [print(i['title'], '|', i.get('status','')) for i in json.load(sys.stdin)['items']]"
```

2. If no relevant card exists, create an issue then add it to the project:
```bash
gh issue create --repo rbrthmn/comfin --title "<title>" --assignee rbrthmn --body "<structured description (see template below)>"
gh project item-add 4 --owner "@me" --url https://github.com/rbrthmn/comfin/issues/<number>
```

3. Set status to **Todo** and get item ID:
```bash
ITEM_ID=$(gh project item-list 4 --owner "@me" --format json | python3 -c "import json,sys; [print(i['id']) for i in json.load(sys.stdin)['items'] if i.get('content',{}).get('number') == <issue-number>]")
gh project item-edit --project-id PVT_kwHOAo5Oes4Akxw8 --id $ITEM_ID --field-id PVTSSF_lAHOAo5Oes4Akxw8zgc7r2s --single-select-option-id f75ad846
```

4. Create branch using issue number:
```bash
git checkout -b feature/<issue-number>-<short-kebab-desc>
```

### When starting implementation

Set status to **In Progress**:
```bash
ITEM_ID=$(gh project item-list 4 --owner "@me" --format json | python3 -c "import json,sys; [print(i['id']) for i in json.load(sys.stdin)['items'] if i.get('content',{}).get('number') == <issue-number>]")
gh project item-edit --project-id PVT_kwHOAo5Oes4Akxw8 --id $ITEM_ID --field-id PVTSSF_lAHOAo5Oes4Akxw8zgc7r2s --single-select-option-id 47fc9ee4
```

### When opening a PR

Set status to **Code Review**:
```bash
ITEM_ID=$(gh project item-list 4 --owner "@me" --format json | python3 -c "import json,sys; [print(i['id']) for i in json.load(sys.stdin)['items'] if i.get('content',{}).get('number') == <issue-number>]")
gh project item-edit --project-id PVT_kwHOAo5Oes4Akxw8 --id $ITEM_ID --field-id PVTSSF_lAHOAo5Oes4Akxw8zgc7r2s --single-select-option-id 5f0eb544
```

### Issue description template

```markdown
## Description
<!-- What this feature/change does and why -->

## Scope
<!-- Modules affected -->

## Acceptance Criteria
- [ ] ...

## Technical Notes
<!-- Implementation approach, relevant classes, data models -->
```

## Key Conventions

- No license headers in any file
- `UiState` is always an immutable `data class` — use `copy()` for updates
- String/dimension/color values come from resources (`R.string`, etc.), never hardcoded in composables
- No comments unless explicitly requested
- Java 17, compileSdk 35, minSdk 26
- Room schemas auto-generated via KSP, stored in `data/common/schemas/`
