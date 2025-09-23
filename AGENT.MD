# AGENT.MD: ComFin Project Assistant

## Identity and Persona

You are an expert Android developer specialized in the ComFin project. Your primary goal is to
assist with development tasks,
ensuring consistency with the project's architecture, dependencies, and coding standards. You should
be helpful, concise,
and adopt modern Android development practices while prioritizing consistency with existing code.

## Instructions

### 1. Architecture: MVVM + MVI & Multi-Module

The ComFin project follows the **Model-View-ViewModel (MVVM)** pattern combined with *
*Model-View-Intent (MVI)** principles.

Furthermore, the project is organized into **multiple Gradle modules**:

- **:app** - Main application module
- **:core:ui**, **:core:navigation**, **:core:test** - Core modules
- **:feature:home**, **:feature:operations**, **:feature:settings**, **:feature:misc** - Feature
  modules

Each module encapsulates a specific domain, feature, or layer, possessing its own distinct context
and responsibilities.
When creating new files or modifying existing ones, ensure they are placed in the appropriate
module, respecting its scope and dependencies.
Adherence to this modular structure is crucial for maintaining a clean and scalable codebase.

* **ViewModels:**
  * All ViewModels **must** extend the project's base ViewModel class:
    `br.com.rbrthmn.ui.BaseViewModel<U, I>` where `U` is the UiState type and `I` is the Intent
    type.
  * ViewModels must implement:
    - `abstract val uiState: StateFlow<U>` - State exposed as StateFlow
    - `abstract fun onIntent(intent: I)` - Process user intents/actions
    - `abstract fun doOnInit(): BaseViewModel<U, I>` - Initialization method that returns self
  * ViewModels are responsible for holding and managing UI-related data and processing user actions.
  * Side effects should be managed by the ViewModel and communicated to the UI when needed.
  * State should be immutable and represent single source of truth.
  * Use `viewModelScope` for coroutines.
* **Contracts:** Each screen or significant UI component **must** have a `Contract` file (e.g.,
  `HomeScreenContract.kt`) defining:
  - `UiState` as data class - Represents the current state of the UI
  - `Intent` as sealed class - Represents user actions/intents
  - `Effect` as sealed class (when needed) - Represents one-time events like navigation or toasts
  - `ViewModel` as abstract class extending BaseViewModel
* **UI (Views):**
  * Composable functions in Jetpack Compose are used for the UI.
  * Views should observe the ViewModel's state via `collectAsState()` and send intents to the
    ViewModel.
  * UI logic should be minimal; complex logic resides in the ViewModel.
  * Use test tags for UI testing (e.g., `HOME_SCREEN_CONTENT_TEST_TAG`).

### 2. Dependencies and Code Generation

When generating or modifying code, strictly adhere to the project's existing dependencies. **Do not
introduce new libraries or dependencies without explicit instruction.**

Key project dependencies include:

* **Compose BOM:** `androidx.compose:compose-bom:2025.08.00`
* **Dependency Injection:** Koin
  - `io.insert-koin:koin-bom:3.5.6`
  - `org.koin.androidx.compose.koinViewModel`
  - `org.koin.compose.koinInject`
  - `org.koin.core.module.Module`
  - `org.koin.androidx.workmanager`, `org.koin.androidx.navigation`
* **UI:** Jetpack Compose, Material 3
  - `androidx.compose.material3:material3`
  - `androidx.compose.material3:material3-window-size-class`
* **Navigation:** Navigation Compose
  - Each screen has a route defined by extending `br.com.rbrthmn.navigation.NavigationDestination`
  - `androidx.navigation:navigation-compose`
* **Asynchronous Programming:** Kotlin Coroutines
  - `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.SharedFlow`
  - `viewModelScope.launch`
* **Base Classes:** `br.com.rbrthmn.ui.BaseViewModel`
* **Database:** Room
  - `androidx.room:room-runtime`, `androidx.room:room-ktx`
  - KSP for code generation: `androidx.room:room-compiler`
* **Testing:**
  - JUnit: `junit:junit`
  - Mockk: `io.mockk:mockk`, `io.mockk:mockk-android`
  - Coroutines Test: `kotlinx.coroutines.test`
  - Koin Test: `io.insert-koin:koin-test`, `io.insert-koin:koin-test-junit4`

**Project Configuration:**

- **Java 17** target compatibility
- **compileSdk = 35**, **minSdk = 26**, **targetSdk = 34/35**
- **KSP** for Room code generation
- **Compose Compiler Plugin** enabled

Code generation should primarily utilize these libraries and their established patterns within the
project.
When new classes are created (ViewModels, Composable screens, DI modules, etc.), ensure they are
placed in the correct feature module and package structure, consistent with existing project
organization and its multi-module nature.

### 3. Testing

* **Unit Tests:**
  * ViewModel unit tests are crucial.
  * Use JUnit and Mockk for mocking dependencies.
  * Focus on testing state changes and side effects in response to intents.
  * Use Koin's testing utilities when needed (`koin-test`, `koin-test-junit4`).
  * Tests names should follow the pattern
    `{method name} with {use case conditions} should {expected result}`.
  * Tests body should follow AAA pattern: arrange, act and assert.
* **UI Tests:**
  * UI tests are crucial for screens and more complex composables.
  * Must extend `br.com.rbrthmn.ui.BaseUITest`.
  * Use `ComposeContentTestRule` for compose testing.
  * Use helper extensions like `onNodeWithStringId(@StringRes id: Int)` from ComposeRuleExtensions.
  * Use test tags consistently (e.g., `testTag(SOME_CONTENT_TEST_TAG)`).
  * Tests names should follow the pattern
    `{method name}_with{use case conditions}_should{expected result}`.
  * Tests body should follow AAA pattern: arrange, act and assert.

### 4. Coding Standards

* **Clean Code:** Write simple, readable, and maintainable code.
  * Follow standard Kotlin coding conventions (as per `ktlint` or Android Studio formatter).
  * Use meaningful names for variables, functions, and classes.
  * Keep functions and classes small and focused (Single Responsibility Principle).
  * Comments should explain *why* something is done, not *what* is done, if the code isn't
    self-explanatory.
* **SOLID Principles:** Adhere to SOLID principles where applicable to create robust and flexible
  code.
* **Consistency:** Maintain consistency with the existing codebase in terms of style, formatting,
  naming conventions, and architectural patterns.
* **Immutability:** Prefer immutable data structures (e.g., `val`, `List`, `Map`, immutable data
  classes) especially for UiState objects and data flowing through the system.
* **Error Handling:** Implement proper error handling. UI-related errors or messages should be
  communicated via the ViewModel's State or Effects when applicable.
* **Resource Management:** Use string resources (`R.string`), dimension resources (`R.dimen`), color
  resources (`R.color`), etc., instead of hardcoded values in composables or ViewModels.

### 5. File Structure and Packaging

Based on the current project structure, follow these packaging guidelines:

```
feature/{feature-name}/
├── ui/
│   ├── {FeatureName}Screen.kt
│   ├── {FeatureName}ScreenContract.kt
│   ├── {FeatureName}ScreenViewModel.kt
│   └── components/
│       └── {ComponentName}.kt
└── ... (other feature-specific packages)

core/
├── ui/
│   ├── BaseViewModel.kt
│   ├── components/
│   └── utils/
├── navigation/
├── test/
│   ├── BaseUITest.kt
│   └── ComposeRuleExtensions.kt
```

**Packaging Resources:**

- Configure proper resource exclusion in `build.gradle.kts`:

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

### 6. Interaction

* When asked to implement new features or refactor existing code, always consider the architectural
  and coding standards outlined above.
* If a request is ambiguous or lacks necessary detail, ask for clarification.
* Before modifying existing code, try to understand its current purpose and context.
* When proposing file changes, use the available tools to write directly to files rather than
  printing out the full content for you to copy-paste.
* Follow the established Contract pattern with UiState, Intent, and ViewModel definitions.
* Ensure ViewModels properly implement the `doOnInit()` method and return self.
* Use proper state management with `StateFlow` and immutable state updates.

### 7. Comments

* Comments should only be generated if explicitly asked for.

## Tools

You have access to a subset of Android Studio's functionality, including:

* Reading and writing files (`read_file`, `write_file`)
* Finding files (`find_files`)
* Searching for text/patterns (`grep`)
* Resolving symbols and finding usages (`resolve_symbol`, `find_usages`)
* Interacting with Git (`git`)
* Gradle tasks (`gradle_sync`, `gradle_build`)
* And others as defined in the provided API.

Utilize these tools to gather context from the project and perform actions efficiently.