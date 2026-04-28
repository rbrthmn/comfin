# ComFin

**ComFin** is a financial companion app designed to help you organize and track your personal finances in a simple and intuitive way, eliminating the need for spreadsheets.

## Features

- Manage and monitor income and expenses
- Track reserves (savings) with yield history
- Monitor credit card invoices and bank balances
- Set income allocations and monthly limits
- Manage recurring expenses and investment portfolio
- Modern UI with Jetpack Compose

## Technologies and Architecture

MVVM + MVI across a multi-module Gradle project.

- **Jetpack Compose** — UI
- **Room** — local persistence
- **Koin** — dependency injection
- **Coroutines + Flow** — async data streams

## Module Structure

```mermaid
graph TD
    app(":app"):::app

    coreui(":core:ui"):::core
    coredata(":core:data"):::core
    corenav(":core:navigation"):::core
    coretest(":core:test"):::core

    home(":feature:home"):::feature
    ops(":feature:operations"):::feature
    settings(":feature:settings"):::feature
    misc(":feature:misc"):::feature

    app --> home
    app --> ops
    app --> settings
    app --> misc
    app --> coredata

    home --> coreui
    home --> corenav
    home --> coredata

    ops --> coreui
    ops --> corenav
    ops --> coredata

    settings --> coreui
    settings --> corenav

    misc --> coreui
    misc --> corenav
    misc --> coredata
    misc --> settings
    misc --> ops

    corenav --> coreui
    corenav --> coretest
    coreui --> coretest

    classDef app fill:#e1d5e7,stroke:#9673a6
    classDef core fill:#dae8fc,stroke:#6c8ebf
    classDef feature fill:#d5e8d4,stroke:#82b366
```

> Full diagram: [`docs/modules_dependency_map.drawio.xml`](docs/modules_dependency_map.drawio.xml)

## Domain Model

```mermaid
classDiagram
    Transaction --> BankAccount : bankAccountId (opt)
    Transaction --> CreditCard : creditCardId (opt)
    Transaction --> Reserve : reserveId (opt)
    ReserveTransaction --> Reserve : reserveId

    class Transaction {
        +Long id
        +LocalDate date
        +String counterparty
        +String notes
        +Double amount
        +String type
        +String category
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

> Full diagram: [`docs/domain-classes.drawio.xml`](docs/domain-classes.drawio.xml)

## Getting Started

Requirements: Android Studio, JDK 17+, Android device or emulator.

```bash
git clone https://github.com/rbrthmn/comfin.git
```

Open in Android Studio and run.

## Contributions

Contributions are welcome. Open issues and submit pull requests.

## Licensing

This project includes code licensed under the Apache License 2.0. See [LICENSE-APACHE](http://www.apache.org/licenses/LICENSE-2.0).
