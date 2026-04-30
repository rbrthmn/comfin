package br.com.rbrthmn.data.operations.model

import java.time.LocalDate

data class OperationItem(
    val id: Long,
    val date: LocalDate,
    val counterparty: String,
    val notes: String?,
    val amount: Double,
    val type: String,
    val category: String,
    val accountName: String?
)
