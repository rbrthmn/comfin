package br.com.rbrthmn.data.home.model

data class CreditCardBillItem(
    val id: Long,
    val name: String,
    val issuerName: String,
    val currentStatementValue: Double,
    val dueDay: Int
)
