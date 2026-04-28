package br.com.rbrthmn.misc.recurringexpenses

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class RecurringExpensesScreenViewModel : RecurringExpensesScreenContract.ViewModel() {
    override var uiState = MutableStateFlow(RecurringExpensesScreenContract.UiState())

    override fun doOnInit(): RecurringExpensesScreenViewModel {
        val expenses = listOf(
            RecurringExpense(description = "Aluguel", value = "1500.00", billingDay = "05"),
            RecurringExpense(description = "Internet", value = "100.00", billingDay = "10"),
            RecurringExpense(description = "Energia", value = "80.00", billingDay = "13")
        )
        uiState.value = RecurringExpensesScreenContract.UiState(
            recurringExpenses = expenses,
            totalExpensesValue = computeTotal(expenses)
        )
        return this
    }

    override fun onIntent(intent: RecurringExpensesScreenContract.Intent) {
        when (intent) {
            RecurringExpensesScreenContract.Intent.OnOpenAddExpenseDialog ->
                uiState.update { it.copy(showAddExpenseDialog = true) }
            RecurringExpensesScreenContract.Intent.OnDismissAddExpenseDialog ->
                dismissDialog()
            is RecurringExpensesScreenContract.Intent.OnNewExpenseDescriptionChange ->
                uiState.update { it.copy(newExpenseDescription = intent.description) }
            is RecurringExpensesScreenContract.Intent.OnNewExpenseValueChange ->
                uiState.update { it.copy(newExpenseValue = intent.value) }
            is RecurringExpensesScreenContract.Intent.OnNewExpenseBillingDayChange ->
                uiState.update { it.copy(newExpenseBillingDay = intent.billingDay) }
            RecurringExpensesScreenContract.Intent.OnSaveNewExpense ->
                onSaveNewExpense()
        }
    }

    private fun onSaveNewExpense() {
        with(uiState.value) {
            if (newExpenseDescription.isBlank() || newExpenseValue.isBlank() || newExpenseBillingDay.isBlank()) return
            val newExpense = RecurringExpense(
                description = newExpenseDescription,
                value = newExpenseValue,
                billingDay = newExpenseBillingDay
            )
            val updatedExpenses = recurringExpenses + newExpense
            uiState.update {
                it.copy(
                    recurringExpenses = updatedExpenses,
                    totalExpensesValue = computeTotal(updatedExpenses),
                    showAddExpenseDialog = false,
                    newExpenseDescription = "",
                    newExpenseValue = "",
                    newExpenseBillingDay = ""
                )
            }
        }
    }

    private fun dismissDialog() {
        uiState.update {
            it.copy(
                showAddExpenseDialog = false,
                newExpenseDescription = "",
                newExpenseValue = "",
                newExpenseBillingDay = ""
            )
        }
    }

    private fun computeTotal(expenses: List<RecurringExpense>): String =
        "%.2f".format(expenses.sumOf { it.value.toDoubleOrNull() ?: 0.0 })
}
