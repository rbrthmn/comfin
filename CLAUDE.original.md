# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this
repository.

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
- `:feature:home` — Balance card, credit card bills, monthly limit widgets
- `:feature:operations` — Transaction management
- `:feature:settings` — App settings
- `:feature:misc` — Income divisions, more features screen

**Data flow:** User action → `onIntent()` → ViewModel → `MutableStateFlow` update → UI
`collectAsState()` recompose

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

Destinations implement `NavigationDestination` (defines `route: String`). Routes registered in
`ComFinNavGraph.kt`. Bottom nav has 3 roots: Home, Operations, More Features.

## Testing

**Unit tests** — JUnit + Mockk. Name pattern: `{method} with {conditions} should {result}`. AAA
body.

**UI tests** — Extend `BaseUITest`. Use `onNodeWithStringId()` helper. Tag composables with
`testTag(SOME_TEST_TAG)`. Name pattern: `{method}_with{conditions}_should{result}`.

No new library dependencies without explicit instruction.

## Key Conventions

- No license headers in any file
- `UiState` is always an immutable `data class` — use `copy()` for updates
- String/dimension/color values come from resources (`R.string`, etc.), never hardcoded in
  composables
- No comments unless explicitly requested
- Java 17, compileSdk 35, minSdk 26
- Room schemas auto-generated via KSP, stored in `app/schemas/`
