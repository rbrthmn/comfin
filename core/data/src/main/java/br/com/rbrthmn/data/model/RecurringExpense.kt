package br.com.rbrthmn.data.model

import java.time.LocalDate

data class RecurringExpense(
    val id: Long = 0,
    val description: String,
    val amount: Double,
    val paymentDay: Int,
    val validUntil: LocalDate?,
    val isPaid: Boolean
)
