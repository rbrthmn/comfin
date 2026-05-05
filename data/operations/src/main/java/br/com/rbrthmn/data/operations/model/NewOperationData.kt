package br.com.rbrthmn.data.operations.model

import java.time.LocalDate

sealed class NewOperationData {
    abstract val counterparty: String
    abstract val amount: Double
    abstract val type: String
    abstract val category: String
    abstract val date: LocalDate
    abstract val notes: String?

    data class BankAccountOperation(
        override val counterparty: String,
        override val amount: Double,
        override val type: String,
        override val category: String,
        override val date: LocalDate,
        override val notes: String? = null,
        val bankAccountId: Long?
    ) : NewOperationData()

    data class CreditCardOperation(
        override val counterparty: String,
        override val amount: Double,
        override val type: String,
        override val category: String,
        override val date: LocalDate,
        override val notes: String? = null,
        val creditCardId: Long?
    ) : NewOperationData()

    data class ReserveOperation(
        override val counterparty: String,
        override val amount: Double,
        override val type: String,
        override val category: String,
        override val date: LocalDate,
        override val notes: String? = null,
        val reserveId: Long?,
        val bankAccountId: Long?
    ) : NewOperationData()
}
