package br.com.rbrthmn.data.model

data class BankAccount(
    val id: Long = 0,
    val name: String,
    val bankName: String,
    val balanceValue: Double,
    val isMainAccount: Boolean
)
