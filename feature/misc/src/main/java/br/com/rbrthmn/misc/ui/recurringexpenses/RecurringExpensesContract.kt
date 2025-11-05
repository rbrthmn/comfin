package br.com.rbrthmn.misc.ui.recurringexpenses

import br.com.rbrthmn.ui.BaseViewModel

data class RecurringExpense(val description: String, val value: String, val billingDay: String)

abstract class RecurringExpensesContract {

    abstract class ViewModel : BaseViewModel<UIState, Intent>()

    data class UIState(
        val expenses: List<RecurringExpense> = emptyList(),
        val totalExpensesValue: String = "0.00",
        val showNewExpenseDialog: Boolean = false,
        val newExpenseDescription: String = "",
        val newExpenseValue: String = "",
        val newExpenseBillingDay: String = "",
        val isNewExpenseDescriptionValid: Boolean = true,
        val isNewExpenseValueValid: Boolean = true,
        val isNewExpenseBillingDayValid: Boolean = true
    )

    sealed class Intent {
        data object OnAddExpenseButtonClick : Intent()
        data object OnSaveNewExpense : Intent()
        data object OnCancelNewExpense : Intent()
        data class OnNewExpenseDescriptionChange(val description: String) : Intent()
        data class OnNewExpenseValueChange(val value: String) : Intent()
        data class OnNewExpenseBillingDayChange(val day: String) : Intent()
    }
}
