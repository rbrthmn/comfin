package br.com.rbrthmn.misc.incomedivisions

data class IncomeDivision(
    val name: String,
    val value: String,
    val canEditValue: Boolean = true,
    val percentage: String,
    val canEditPercentage: Boolean = true,
    val isRecurringExpenses: Boolean = false
)
