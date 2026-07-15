# Spec-Driven Architecture

Specs are the source of truth. Code and tests derive from specs, not the other way around.

## Workflow

```
spec/*.yaml  →  *Contract.kt  →  *ViewModel.kt  →  *ViewModelTest.kt
```

1. Write the spec first — define state, intents, and behaviors
2. Implement `*Contract.kt` to match the spec exactly
3. Implement the ViewModel against the contract
4. Write tests whose names match the behavior `description` fields

## Directory Structure

```
specs/
├── README.md            ← this file
├── _template.yaml       ← copy when creating a new spec
└── feature/
    ├── home/
    │   ├── home_screen.yaml
    │   ├── balance_card.yaml
    │   ├── credit_card_bills_card.yaml
    │   ├── last_month_difference_card.yaml
    │   └── monthly_limit_card.yaml
    ├── operations/
    ├── misc/
    └── settings/
```

## Spec Format

Each spec has four sections:

| Section | Maps to |
|---------|---------|
| `state` | `data class *UiState(...)` in Contract.kt |
| `intents` | `sealed class Intent` in Contract.kt |
| `effects` | `sealed class Effect` in Contract.kt (optional) |
| `behaviors` | `@Test fun` in *ViewModelTest.kt |

## Conventions

### Behavior IDs
Format: `{PREFIX}-{NNN}` where prefix = abbreviated feature name.
- `BC` = BalanceCard
- `CCB` = CreditCardBillsCard
- `LMD` = LastMonthDifferenceCard
- `ML` = MonthlyLimitCard
- `HS` = HomeScreen

### Behaviors state business rules only
A behavior describes an outcome the user or domain cares about, observable through
`UiState` or `Effect` — never how the ViewModel gets there.

- Phrase the `description` and `then` as a rule: "should flag balance as required",
  "should add the new card and close the dialog", "should not attempt sign in"
- Never "should assign correctly" / "should set field X" — that describes plumbing, not a rule
- No echo assertions: a field merely mirroring the typed input is not a behavior
- No implementation details in `then`: internal calls, constants, or field-by-field resets.
  Interaction with a dependency belongs in a behavior only when it *is* the rule
  (e.g. "no sign-in attempt made" for an invalid form)
- One behavior per rule — merge cases that only re-assert the same rule from another angle

### Test naming
Test method names must match the behavior `description` field verbatim:
```kotlin
@Test
fun `doOnInit should display the total balance and account balances`() { ... }
// ↑ matches behavior id BC-001 description
```
Test bodies assert only what the behavior's `then` states.

### Adding a new feature
1. Copy `specs/_template.yaml` to `specs/feature/{module}/{feature_name}.yaml`
2. Fill in all sections
3. Implement `*Contract.kt` from the `state` and `intents` sections
4. Write ViewModel behaviors guided by the `behaviors` section
5. Write tests whose names match behavior descriptions
