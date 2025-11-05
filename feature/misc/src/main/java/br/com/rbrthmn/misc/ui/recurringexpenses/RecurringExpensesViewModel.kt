package br.com.rbrthmn.misc.ui.recurringexpenses

import br.com.rbrthmn.ui.utils.canBeFormatted
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class RecurringExpensesViewModel : RecurringExpensesContract.ViewModel() {

    override val uiState = MutableStateFlow(RecurringExpensesContract.UIState())

    override fun doOnInit(): RecurringExpensesContract.ViewModel {
        val initialExpenses = listOf(
            RecurringExpense(
                description = "Aluguel",
                value = "1500.00",
                billingDay = "05"
            ),
            RecurringExpense(
                description = "Internet",
                value = "100.00",
                billingDay = "10"
            ),
            RecurringExpense(
                description = "Energia",
                value = "80.00",
                billingDay = "13"
            )
        )
        val total = initialExpenses.sumOf { it.value.toDouble() }.toString()

        uiState.update {
            it.copy(
                expenses = initialExpenses,
                totalExpensesValue = total
            )
        }
        return this
    }

    override fun onIntent(intent: RecurringExpensesContract.Intent) {
        when (intent) {
            is RecurringExpensesContract.Intent.OnAddExpenseButtonClick -> onAddExpenseButtonClick()
            is RecurringExpensesContract.Intent.OnCancelNewExpense -> onCancelNewExpense()
            is RecurringExpensesContract.Intent.OnSaveNewExpense -> onSaveNewExpense()
            is RecurringExpensesContract.Intent.OnNewExpenseDescriptionChange -> onNewExpenseDescriptionChange(
                intent.description
            )

            is RecurringExpensesContract.Intent.OnNewExpenseValueChange -> onNewExpenseValueChange(
                intent.value
            )

            is RecurringExpensesContract.Intent.OnNewExpenseBillingDayChange -> onNewExpenseBillingDayChange(
                intent.day
            )
        }
    }

    private fun onAddExpenseButtonClick() {
        uiState.update { it.copy(showNewExpenseDialog = true) }
    }

    private fun onCancelNewExpense() {
        uiState.update {
            it.copy(
                showNewExpenseDialog = false,
                newExpenseDescription = "",
                newExpenseValue = "",
                newExpenseBillingDay = "",
                isNewExpenseDescriptionValid = true,
                isNewExpenseValueValid = true,
                isNewExpenseBillingDayValid = true
            )
        }
    }

    private fun onSaveNewExpense() {
        val currentState = uiState.value
        val isDescriptionValid = currentState.newExpenseDescription.isNotBlank()
        val isValueValid =
            currentState.newExpenseValue.isNotBlank() && canBeFormatted(currentState.newExpenseValue)
        val isBillingDayValid = currentState.newExpenseBillingDay.isNotBlank()

        uiState.update {
            it.copy(
                isNewExpenseDescriptionValid = isDescriptionValid,
                isNewExpenseValueValid = isValueValid,
                isNewExpenseBillingDayValid = isBillingDayValid
            )
        }

        if (isDescriptionValid && isValueValid && isBillingDayValid) {
            val newExpense = RecurringExpense(
                description = currentState.newExpenseDescription,
                value = currentState.newExpenseValue,
                billingDay = currentState.newExpenseBillingDay
            )
            val updatedExpenses = currentState.expenses + newExpense
            val newTotal = updatedExpenses.sumOf { it.value.toDouble() }.toString()

            uiState.update {
                it.copy(
                    expenses = updatedExpenses,
                    totalExpensesValue = newTotal,
                    showNewExpenseDialog = false,
                    newExpenseDescription = "",
                    newExpenseValue = "",
                    newExpenseBillingDay = "",
                    isNewExpenseDescriptionValid = true,
                    isNewExpenseValueValid = true,
                    isNewExpenseBillingDayValid = true
                )
            }
        }
    }

    private fun onNewExpenseDescriptionChange(description: String) {
        uiState.update {
            it.copy(
                newExpenseDescription = description,
                isNewExpenseDescriptionValid = description.isNotBlank()
            )
        }
    }

    private fun onNewExpenseValueChange(value: String) {
        uiState.update {
            it.copy(
                newExpenseValue = value,
                isNewExpenseValueValid = value.isNotBlank() && canBeFormatted(value)
            )
        }
    }

    private fun onNewExpenseBillingDayChange(day: String) {
        uiState.update {
            it.copy(
                newExpenseBillingDay = day,
                isNewExpenseBillingDayValid = day.isNotBlank()
            )
        }
    }
}
