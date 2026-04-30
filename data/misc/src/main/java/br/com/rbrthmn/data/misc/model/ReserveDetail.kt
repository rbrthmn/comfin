package br.com.rbrthmn.data.misc.model

import java.time.LocalDate

data class ReserveDetail(
    val id: Long,
    val name: String,
    val institution: String,
    val currentTotal: Double,
    val monthlyYield: Double,
    val totalInflow: Double,
    val totalOutflow: Double,
    val transactions: List<ReserveTransactionItem>
)

data class ReserveTransactionItem(
    val id: Long,
    val date: LocalDate,
    val inflow: Double,
    val outflow: Double,
    val yield: Double,
    val value: Double
)
