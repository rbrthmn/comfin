package br.com.rbrthmn.data.model

import java.time.LocalDate

data class ReserveTransaction(
    val id: Long = 0,
    val reserveId: Long,
    val date: LocalDate,
    val inflow: Double,
    val outflow: Double,
    val yield: Double,
    val value: Double
)
