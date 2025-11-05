package br.com.rbrthmn.misc.ui.incomedivisions

data class IncomeDivision(
    val name: String,
    val value: String,
    val percentage: String,
    val canEditValue: Boolean = false,
    val canEditPercentage: Boolean = true,
    val isRecurringExpenses: Boolean = false,
    val onRecurringExpensesClick: () -> Unit = {}
)
