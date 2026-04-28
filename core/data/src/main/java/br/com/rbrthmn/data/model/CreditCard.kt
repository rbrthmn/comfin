package br.com.rbrthmn.data.model

data class CreditCard(
    val id: Long = 0,
    val name: String,
    val issuerName: String,
    val currentStatementValue: Double,
    val availableLimit: Double,
    val dueDay: Int
)
