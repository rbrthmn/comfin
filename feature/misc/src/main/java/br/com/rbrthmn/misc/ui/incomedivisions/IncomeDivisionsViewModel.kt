package br.com.rbrthmn.misc.ui.incomedivisions

import br.com.rbrthmn.ui.utils.canBeFormatted
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class IncomeDivisionsViewModel : IncomeDivisionsContract.ViewModel() {

    override val uiState = MutableStateFlow(IncomeDivisionsContract.UIState())

    override fun doOnInit(): IncomeDivisionsViewModel {
        uiState.update {
            it.copy(
                incomeDivisions = listOf(
                    IncomeDivision(
                        name = "Total income",
                        value = "1000",
                        percentage = "100",
                        canEditValue = true,
                        canEditPercentage = false,
                    ),
                    IncomeDivision(
                        name = "Recurring expenses",
                        value = "100",
                        percentage = "10",
                        canEditValue = false,
                        isRecurringExpenses = true,
                    ),
                    IncomeDivision(
                        name = "Remaining for the month",
                        value = "100",
                        canEditValue = false,
                        percentage = "10",
                        canEditPercentage = false,
                    ),
                )
            )
        }
        return this
    }

    override fun onIntent(intent: IncomeDivisionsContract.Intent) {
        when (intent) {
            is IncomeDivisionsContract.Intent.OnAddDivisionButtonClick -> onAddDivisionButtonClick()
            is IncomeDivisionsContract.Intent.OnCancelNewDivision -> onCancelNewDivision()
            is IncomeDivisionsContract.Intent.OnSaveNewDivision -> onSaveNewDivision()
            is IncomeDivisionsContract.Intent.OnNewDivisionNameChange -> onNewDivisionNameChange(
                intent.name
            )

            is IncomeDivisionsContract.Intent.OnNewDivisionValueChange -> onNewDivisionValueChange(
                intent.value
            )

            is IncomeDivisionsContract.Intent.OnNewDivisionPercentageChange -> onNewDivisionPercentageChange(
                intent.percentage
            )

            is IncomeDivisionsContract.Intent.OnDivisionValueChanged -> onDivisionValueChanged(
                intent.index,
                intent.value
            )

            is IncomeDivisionsContract.Intent.OnDivisionPercentageChanged -> onDivisionPercentageChanged(
                intent.index,
                intent.percentage
            )
        }
    }

    private fun onDivisionValueChanged(index: Int, value: String) {
        uiState.update { currentState ->
            val updatedDivisions = currentState.incomeDivisions.toMutableList()
            if (index >= 0 && index < updatedDivisions.size) {
                updatedDivisions[index] = updatedDivisions[index].copy(value = value)
            }
            currentState.copy(incomeDivisions = updatedDivisions)
        }
    }

    private fun onDivisionPercentageChanged(index: Int, percentage: String) {
        uiState.update { currentState ->
            val updatedDivisions = currentState.incomeDivisions.toMutableList()
            if (index >= 0 && index < updatedDivisions.size) {
                updatedDivisions[index] = updatedDivisions[index].copy(percentage = percentage)
            }
            currentState.copy(incomeDivisions = updatedDivisions)
        }
    }

    private fun onAddDivisionButtonClick() {
        uiState.update { it.copy(showNewDivisionDialog = true) }
    }

    private fun onCancelNewDivision() {
        uiState.update {
            it.copy(
                showNewDivisionDialog = false,
                newDivisionName = "",
                newDivisionValue = "",
                newDivisionPercentage = "",
                isNewDivisionNameValid = true,
                isNewDivisionValueValid = true,
                isNewDivisionPercentageValid = true
            )
        }
    }

    private fun onSaveNewDivision() {
        val currentState = uiState.value
        val isNameValid = currentState.newDivisionName.isNotBlank()
        val isValueValid =
            currentState.newDivisionValue.isNotBlank() && canBeFormatted(currentState.newDivisionValue)
        val isPercentageValid = currentState.newDivisionPercentage.isNotBlank()

        uiState.update {
            it.copy(
                isNewDivisionNameValid = isNameValid,
                isNewDivisionValueValid = isValueValid,
                isNewDivisionPercentageValid = isPercentageValid
            )
        }

        if (isNameValid && isValueValid && isPercentageValid) {
            val newDivision = IncomeDivision(
                name = currentState.newDivisionName,
                value = currentState.newDivisionValue,
                percentage = currentState.newDivisionPercentage
            )
            uiState.update {
                val insertionIndex = it.incomeDivisions.size - 1
                val newDivisions = it.incomeDivisions.toMutableList().apply {
                    add(insertionIndex, newDivision)
                }
                it.copy(
                    incomeDivisions = newDivisions,
                    showNewDivisionDialog = false,
                    newDivisionName = "",
                    newDivisionValue = "",
                    newDivisionPercentage = "",
                    isNewDivisionNameValid = true,
                    isNewDivisionValueValid = true,
                    isNewDivisionPercentageValid = true
                )
            }
        }
    }

    private fun onNewDivisionNameChange(name: String) {
        uiState.update {
            it.copy(
                newDivisionName = name,
                isNewDivisionNameValid = name.isNotBlank()
            )
        }
    }

    private fun onNewDivisionValueChange(value: String) {
        uiState.update {
            it.copy(
                newDivisionValue = value,
                isNewDivisionValueValid = value.isNotBlank() && canBeFormatted(value)
            )
        }
    }

    private fun onNewDivisionPercentageChange(percentage: String) {
        uiState.update { it.copy(newDivisionPercentage = percentage) }
    }
}
