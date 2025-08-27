package br.com.rbrthmn.home.ui.components.creditcardbillscard

import androidx.compose.runtime.MutableState
import br.com.rbrthmn.ui.BaseViewModel
import br.com.rbrthmn.ui.R
import java.time.LocalDate

interface CreditCardBillsCardContract {
    abstract class ViewModel : BaseViewModel<CreditCardsBillCardUiState, Intent>()

    sealed class Intent {
        data class OnNewCreditCardNameChange(val name: String) : Intent()
        data class OnNewCreditCardBillValueChange(val bill: String) : Intent()
        data class OnBankChange(val bankIcon: Int, val bankName: String) : Intent()
        data class OnNewCreditCardBillDueDayChange(val day: Int) : Intent()
        data class OnSaveClick(val showDialog: MutableState<Boolean>) : Intent()
        data object CleanInputs : Intent()
        data class OnDateFilterChange(val date: LocalDate) : Intent()
    }

    data class CreditCardBillUiState(
        val name: String = "",
        val value: String = "",
        val bankName: String = "",
        val bankIcon: Int = R.drawable.bank_icon,
        val canValueBeEdited: Boolean = false,
        val dueDay: Int = 0
    )

    data class CreditCardsBillCardUiState(
        val totalBill: String = "",
        val bills: List<CreditCardBillUiState> = listOf(),
        val newCreditCardName: String = "",
        val isNewCreditCardNameValid: Boolean = true,
        val newCreditCardBill: String = "",
        val isNewCreditCardBillValid: Boolean = true,
        val newCreditCardBillDueDay: Int = 0,
        val isNewCreditCardBillDueDayValid: Boolean = true,
        val newCreditCardBankName: String = "",
        val isNewCreditCardBankNameValid: Boolean = true,
        val newCreditCardBankIcon: Int = R.drawable.bank_icon,
        var currentDateFilter: LocalDate = LocalDate.now()
    )
}
