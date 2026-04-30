package br.com.rbrthmn.data.misc.model

import java.time.LocalDate

data class RecurringExpenseDetail(
    val id: Long,
    val description: String,
    val amount: Double,
    val paymentDay: Int,
    val validUntil: LocalDate?,
    val isPaid: Boolean
)
