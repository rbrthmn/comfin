# CLAUDE.md

Guidance for Claude Code (claude.ai/code) when working in this repo.

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

Android personal finance app. Kotlin + Jetpack Compose. MVVM + MVI. Multi-module Gradle.

**Module layout:**
- `:app` — App entry, Koin setup, NavGraph, MainActivity
- `:core:ui` — `BaseViewModel`, shared composables, theme, utils
- `:core:navigation` — `NavigationDestination`, `ComFinNavigationBar`
- `:core:test` — `BaseUITest`, `ComposeRuleExtensions`
- `:data:common` — Room DB, all entities, all DAOs, TypeConverters, Koin DAO bindings
- `:data:home` — `HomeRepository` + `LocalHomeDataSource` (accounts summary, credit card bills)
- `:data:operations` — `OperationsRepository` + `LocalOperationsDataSource` (assembled ops+account names)
- `:data:misc` — `MiscRepository` + `LocalMiscDataSource` (reserves, investments, allocations, recurring)
- `:feature:home` — Balance card, credit card bills, monthly limit widgets
- `:feature:operations` — Transaction management
- `:feature:settings` — App settings
- `:feature:misc` — Income divisions, more features screen

**Feature internal structure:**
```
feature/{name}/
├── di/{Name}Module.kt
└── ui/
    ├── {Name}Screen.kt
    ├── {Name}ScreenContract.kt
    ├── {Name}ScreenViewModel.kt
    └── components/
        └── {Component}.kt
```

**Data layer pattern:** Each `data:X` module has `repository/` (interface) + `local/Local{X}DataSource` (Room impl). Swap local→remote via DI when API ready. Feature modules depend only on their corresponding `data:X`.

**Data flow:** User action → `onIntent()` → ViewModel → `MutableStateFlow` update → UI `collectAsState()` recompose

## Contract Pattern

Every screen needs `*Contract.kt` with three types + one abstract class:

```kotlin
object HomeScreenContract {
    data class UiState(...)           // immutable, single source of truth
    sealed class Intent { ... }       // user actions
    sealed class Effect { ... }       // one-time events (navigation, toasts)
    abstract class ViewModel : BaseViewModel<UiState, Intent>()
}
```

## ViewModel Rules

All ViewModels extend `br.com.rbrthmn.ui.BaseViewModel<U, I>`. Must implement:
- `abstract val uiState: StateFlow<U>` — expose read-only
- `abstract fun onIntent(intent: I)` — handle intents
- `abstract fun doOnInit(): BaseViewModel<U, I>` — init, returns `this`

Use `viewModelScope` for coroutines. Internal state via `MutableStateFlow`, exposed as `StateFlow`.

## DI (Koin)

Each feature module has `di/{Feature}Module.kt`. Modules collected in `ComFin` Application class.

Inject in Compose: `koinViewModel()` / `koinInject()`.

## Navigation

Destinations implement `NavigationDestination` (defines `route: String`). Routes registered in `ComFinNavGraph.kt`. Bottom nav: 3 roots — Home, Operations, More Features.

## Testing

**Unit tests** — JUnit + Mockk. Name: `{method} with {conditions} should {result}`. AAA body. Use `koin-test` / `koin-test-junit4` when DI needed.

**UI tests** — Extend `BaseUITest`. Use `onNodeWithStringId()` helper. Tag composables with `testTag(SOME_TEST_TAG)`. Name: `{method}_with{conditions}_should{result}`.

No new library dependencies without explicit instruction.

**Key deps:** Compose BOM · Koin BOM · Room + KSP · Coroutines · Mockk · Navigation Compose

## Key Conventions

- No license headers in any file
- `UiState` always immutable `data class` — use `copy()` for updates
- String/dimension/color from resources (`R.string`, etc.), never hardcoded in composables
- No comments unless asked
- Java 17, compileSdk 35, minSdk 26
- Room schemas auto-generated via KSP, stored in `data/common/schemas/`
- UI errors/messages → ViewModel `UiState` or `Effect`, never raw exceptions to UI
- Follow Kotlin coding conventions (Android Studio formatter / ktlint)
- New `build.gradle.kts` library modules must include packaging exclusions:

```kotlin
packaging {
    resources {
        excludes += "/META-INF/{AL2.0,LGPL2.1}"
        excludes += "META-INF/LICENSE*"
        excludes += "META-INF/NOTICE*"
        excludes += "META-INF/junit-platform.properties"
        excludes += "META-INF/junit-jupiter-*.properties"
    }
}
```