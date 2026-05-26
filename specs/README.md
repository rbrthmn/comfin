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

### Test naming
Test method names must match the behavior `description` field verbatim:
```kotlin
@Test
fun `doOnInit should collect accounts from repository and update state`() { ... }
// ↑ matches behavior id BC-001 description
```

### Adding a new feature
1. Copy `specs/_template.yaml` to `specs/feature/{module}/{feature_name}.yaml`
2. Fill in all sections
3. Implement `*Contract.kt` from the `state` and `intents` sections
4. Write ViewModel behaviors guided by the `behaviors` section
5. Write tests whose names match behavior descriptions
