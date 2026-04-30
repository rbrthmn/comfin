# Data Layer

## Module Overview

| Module | Responsibility |
|---|---|
| `:data:common` | Room DB, all entities, all DAOs, TypeConverters — shared infrastructure |
| `:data:home` | `HomeRepository` — account summary, credit card bills |
| `:data:operations` | `OperationsRepository` — monthly transactions assembled with account names |
| `:data:misc` | `MiscRepository` — reserves + transactions, investments, allocations, recurring expenses |

## Pattern

Each domain module exposes a `Repository` interface backed by a `Local{X}DataSource` that assembles data from DAOs internally. ViewModels receive ready-to-use objects — no joins in UI layer.

```
feature:X  →  data:X (Repository interface)
                  ↓
           Local{X}DataSource  (Room, current)
           Remote{X}DataSource (Retrofit, future)
                  ↓
           data:common (DAOs + DB)
```

Swapping local → remote: rebind the interface in Koin. Zero ViewModel changes.

## Entity Relationships

```mermaid
classDiagram
    Transaction --> BankAccount : bankAccountId (opt, SET_NULL)
    Transaction --> CreditCard : creditCardId (opt, SET_NULL)
    Transaction --> Reserve : reserveId (opt, SET_NULL)
    ReserveTransaction --> Reserve : reserveId (CASCADE)

    class Transaction {
        +Long id
        +LocalDate date
        +String counterparty
        +String notes
        +Double amount
        +String type
        +String category
        +Long bankAccountId
        +Long creditCardId
        +Long reserveId
    }
    class BankAccount {
        +Long id
        +String name
        +String bankName
        +Double balanceValue
        +Boolean isMainAccount
    }
    class CreditCard {
        +Long id
        +String name
        +String issuerName
        +Double currentStatementValue
        +Double availableLimit
        +Int dueDay
    }
    class Reserve {
        +Long id
        +String name
        +String institution
        +Double currentTotal
        +Double monthlyYield
        +Double totalInflow
        +Double totalOutflow
    }
    class ReserveTransaction {
        +Long id
        +Long reserveId
        +LocalDate date
        +Double inflow
        +Double outflow
        +Double yield
        +Double value
    }
    class RecurringExpense {
        +Long id
        +String description
        +Double amount
        +Int paymentDay
        +LocalDate validUntil
        +Boolean isPaid
    }
    class Allocation {
        +Long id
        +String name
        +Double plannedPercentage
        +Double targetValue
        +Double actualAllocated
    }
    class Investment {
        +Long id
        +String type
        +String institution
        +String assetName
        +LocalDate purchaseDate
        +LocalDate expiryDate
        +Double quantity
        +Double proventos
        +Double investedValue
        +Double currentValue
    }
```

## Database

- **File:** `comfin.db`
- **Version:** 1
- **Schema exports:** `data/common/schemas/`
- All dates stored as `Long` (epoch day) via `DatabaseConverters`
