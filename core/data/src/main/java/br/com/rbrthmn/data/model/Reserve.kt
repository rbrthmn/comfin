package br.com.rbrthmn.data.model

data class Reserve(
    val id: Long = 0,
    val name: String,
    val institution: String,
    val currentTotal: Double,
    val monthlyYield: Double,
    val totalInflow: Double,
    val totalOutflow: Double
)
