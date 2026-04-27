package br.com.rbrthmn.misc.recurringexpenses

import br.com.rbrthmn.ui.BaseViewModel

interface RecurringExpensesScreenContract {
    abstract class ViewModel : BaseViewModel<UiState, Intent>()

    data class UiState(
        val recurringExpenses: List<RecurringExpense> = emptyList(),
        val totalExpensesValue: String = "",
        val showAddExpenseDialog: Boolean = false,
        val newExpenseDescription: String = "",
        val newExpenseValue: String = "",
        val newExpenseBillingDay: String = ""
    )

    sealed class Intent {
        object OnOpenAddExpenseDialog : Intent()
        object OnDismissAddExpenseDialog : Intent()
        data class OnNewExpenseDescriptionChange(val description: String) : Intent()
        data class OnNewExpenseValueChange(val value: String) : Intent()
        data class OnNewExpenseBillingDayChange(val billingDay: String) : Intent()
        object OnSaveNewExpense : Intent()
    }
}
