package br.com.rbrthmn.data.home.model

data class AccountsSummary(
    val totalBalance: Double,
    val accounts: List<AccountSummaryItem>
)

data class AccountSummaryItem(
    val id: Long,
    val name: String,
    val bankName: String,
    val balance: Double,
    val isMainAccount: Boolean
)
