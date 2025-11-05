package br.com.rbrthmn.home.ui.components.balancecard

import androidx.compose.runtime.MutableState
import br.com.rbrthmn.ui.BaseViewModel
import br.com.rbrthmn.ui.R
import java.time.LocalDate

interface BalanceCardContract {
    abstract class ViewModel : BaseViewModel<BalanceCardUiState, Intent>()

    sealed class Intent {
        data class OnInitialBalanceChange(val balance: String) : Intent()
        data class OnDescriptionChange(val description: String) : Intent()
        data class OnBankChange(val bankId: Int, val bankName: String) : Intent()
        data class OnSaveClick(val showDialog: MutableState<Boolean>) : Intent()
        data object CleanNewAccount : Intent()
        data class OnDateFilterChange(val date: LocalDate) : Intent()
    }

    data class BalanceCardUiState(
        val totalBalance: String = "",
        val accounts: List<BankAccountBalanceUiState> = listOf(),
        val newAccountBalance: String = "",
        val isNewAccountBalanceValid: Boolean = true,
        val newAccountDescription: String = "",
        val isNewAccountDescriptionValid: Boolean = true,
        val newAccountBank: String = "",
        val isNewAccountBankValid: Boolean = true,
        val newAccountBankIcon: Int = R.drawable.bank_icon,
        val currentDateFilter: LocalDate = LocalDate.now()
    )

    data class BankAccountBalanceUiState(
        val name: String = "",
        val value: String = "",
        val bankName: String = "",
        val bankIcon: Int = R.drawable.bank_icon,
        val canValueBeEdited: Boolean = false
    )
}
