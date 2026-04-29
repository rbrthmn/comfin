package br.com.rbrthmn.data.wealth.model

import java.time.LocalDate

data class Investment(
    val id: Long = 0,
    val type: String,
    val institution: String,
    val assetName: String,
    val purchaseDate: LocalDate?,
    val expiryDate: LocalDate?,
    val quantity: Double,
    val proventos: Double,
    val investedValue: Double,
    val currentValue: Double
)
