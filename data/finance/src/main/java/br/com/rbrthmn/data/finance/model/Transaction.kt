package br.com.rbrthmn.data.finance.model

import java.time.LocalDate

data class Transaction(
    val id: Long = 0,
    val date: LocalDate,
    val counterparty: String,
    val notes: String?,
    val amount: Double,
    val type: String,
    val category: String,
    val reserveId: Long?,
    val creditCardId: Long?,
    val bankAccountId: Long?
)
