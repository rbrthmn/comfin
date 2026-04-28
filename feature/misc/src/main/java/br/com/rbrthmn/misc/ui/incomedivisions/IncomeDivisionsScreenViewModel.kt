package br.com.rbrthmn.misc.incomedivisions

import br.com.rbrthmn.misc.R
import br.com.rbrthmn.ui.utils.StringProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class IncomeDivisionsScreenViewModel(private val stringProvider: StringProvider) :
    IncomeDivisionsScreenContract.ViewModel() {

    override var uiState = MutableStateFlow(IncomeDivisionsScreenContract.UiState())

    override fun doOnInit(): IncomeDivisionsScreenViewModel {
        uiState.value = IncomeDivisionsScreenContract.UiState(
            incomeDivisions = listOf(
                IncomeDivision(
                    name = stringProvider.getString(R.string.total_income),
                    value = "1000",
                    percentage = "100",
                    canEditPercentage = false,
                ),
                IncomeDivision(
                    name = stringProvider.getString(R.string.recurring_expenses),
                    value = "100",
                    percentage = "10",
                    canEditValue = false,
                    isRecurringExpenses = true,
                ),
                IncomeDivision(
                    name = stringProvider.getString(R.string.remaining_month),
                    value = "100",
                    canEditValue = false,
                    percentage = "10",
                    canEditPercentage = false,
                )
            )
        )
        return this
    }

    override fun onIntent(intent: IncomeDivisionsScreenContract.Intent) {
        when (intent) {
            IncomeDivisionsScreenContract.Intent.OnOpenAddDivisionDialog ->
                uiState.update { it.copy(showAddDivisionDialog = true) }
            IncomeDivisionsScreenContract.Intent.OnDismissAddDivisionDialog ->
                dismissDialog()
            is IncomeDivisionsScreenContract.Intent.OnNewDivisionNameChange ->
                uiState.update { it.copy(newDivisionName = intent.name) }
            is IncomeDivisionsScreenContract.Intent.OnNewDivisionValueChange ->
                uiState.update { it.copy(newDivisionValue = intent.value) }
            is IncomeDivisionsScreenContract.Intent.OnNewDivisionPercentageChange ->
                uiState.update { it.copy(newDivisionPercentage = intent.percentage) }
            IncomeDivisionsScreenContract.Intent.OnSaveNewDivision ->
                onSaveNewDivision()
            is IncomeDivisionsScreenContract.Intent.OnDateSelected ->
                uiState.update { it.copy(selectedDate = intent.date) }
        }
    }

    private fun onSaveNewDivision() {
        with(uiState.value) {
            if (newDivisionName.isBlank() || newDivisionValue.isBlank() || newDivisionPercentage.isBlank()) return
            val newDivision = IncomeDivision(
                name = newDivisionName,
                value = newDivisionValue,
                percentage = newDivisionPercentage
            )
            val updatedList = incomeDivisions.toMutableList().also {
                it.add(it.lastIndex, newDivision)
            }
            uiState.update {
                it.copy(
                    incomeDivisions = updatedList,
                    showAddDivisionDialog = false,
                    newDivisionName = "",
                    newDivisionValue = "",
                    newDivisionPercentage = ""
                )
            }
        }
    }

    private fun dismissDialog() {
        uiState.update {
            it.copy(
                showAddDivisionDialog = false,
                newDivisionName = "",
                newDivisionValue = "",
                newDivisionPercentage = ""
            )
        }
    }
}
