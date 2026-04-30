package br.com.rbrthmn.data.misc.model

import java.time.LocalDate

data class InvestmentDetail(
    val id: Long,
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
