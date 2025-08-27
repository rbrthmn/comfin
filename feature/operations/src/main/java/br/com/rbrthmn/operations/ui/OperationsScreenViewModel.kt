
package br.com.rbrthmn.operations.ui

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import br.com.rbrthmn.operations.R
import br.com.rbrthmn.operations.ui.components.AccountsDropdownMenu
import br.com.rbrthmn.operations.ui.components.OperationAimedAccount
import br.com.rbrthmn.operations.ui.components.OperationOriginAccount
import br.com.rbrthmn.operations.ui.components.utils.getOperationsMock
import br.com.rbrthmn.ui.components.ReservesDropdownMenu
import br.com.rbrthmn.ui.utils.StringProvider
import br.com.rbrthmn.ui.utils.canBeFormatted
import br.com.rbrthmn.ui.utils.formatDouble
import br.com.rbrthmn.ui.utils.formatString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class OperationsScreenViewModel(val stringProvider: StringProvider) :
    OperationsScreenContract.ViewModel() {
    override var uiState = MutableStateFlow(OperationsScreenContract.UiState())

    override fun doOnInit(): OperationsScreenViewModel {
        uiState.value =
            OperationsScreenContract.UiState(
                operations = getOperationsMock().sortedByDescending { it.date },
                totalBalance = formatDouble(TOTAL_BALANCE_MOCK),
                totalIncome = formatDouble(TOTAL_INCOME_MOCK),
                totalOutcome = formatDouble(TOTAL_OUTCOME_MOCK)
            )
        updateDialogFields(resetFields = true)

        return this
    }

    override fun onIntent(intent: OperationsScreenContract.Intent) {
        when (intent) {
            is OperationsScreenContract.Intent.OnDescriptionChange -> onDescriptionChange(intent.description)
            is OperationsScreenContract.Intent.OnValueChange -> onValueChange(intent.value)
            is OperationsScreenContract.Intent.OnOperationTypeChange -> onOperationTypeChange(intent.operationType)
            is OperationsScreenContract.Intent.OnOriginAccountChange -> onOriginAccountChange(intent.originAccount)
            is OperationsScreenContract.Intent.OnDestinationAccountChange -> onDestinationAccountChange(
                intent.destinationAccount
            )

            is OperationsScreenContract.Intent.OnOperationDateChange -> onOperationDateChange(intent.operationDate)
            is OperationsScreenContract.Intent.OnReserveChange -> onReserveChange(intent.reserve)
            is OperationsScreenContract.Intent.OnSaveButtonClick -> onSaveButtonClick(intent.showDialog)
            is OperationsScreenContract.Intent.OnResetDialogFields -> updateDialogFields(resetFields = true)
            is OperationsScreenContract.Intent.OnSearchQueryChange -> onSearchQueryChange(intent.query)
            is OperationsScreenContract.Intent.OnDateFilterChange -> onDateFilterChange(intent.localDate)
        }
    }

    private fun onDescriptionChange(description: String) = uiState.update {
        it.copy(
            newOperationDescription = description,
            isNewOperationDescriptionValid = description.isNotBlank()
        )
    }

    private fun onValueChange(value: String) = uiState.update {
        it.copy(
            newOperationValue = value,
            isNewOperationValueValid = canBeFormatted(value)
        )
    }

    private fun onOperationTypeChange(operationType: OperationType) {
        uiState.update {
            it.copy(
                newOperationType = operationType,
                isNewOperationTypeValid = true
            )
        }
        updateDialogFields(resetFields = true)
    }

    private fun onOriginAccountChange(originAccount: String) {
        if (uiState.value.newOperationType?.hasOriginAccount == true) {
            uiState.update {
                it.copy(
                    isNewOperationOriginAccountValid = originAccount.isNotBlank(),
                    newOperationOriginAccount = originAccount
                )
            }
        }
    }

    private fun onDestinationAccountChange(destinationAccount: String) {
        if (uiState.value.newOperationType?.hasDestinationAccount == true) {
            uiState.update {
                it.copy(
                    isNewOperationDestinationAccountValid = destinationAccount.isNotBlank(),
                    newOperationDestinationAccount = destinationAccount
                )
            }
        }
    }

    private fun onOperationDateChange(operationDate: LocalDate) = uiState.update {
        it.copy(
            newOperationDate = operationDate,
            isNewOperationDateValid = true
        )
    }

    private fun onReserveChange(reserve: String) {
        if (uiState.value.newOperationType?.hasReserve == true) {
            uiState.update {
                it.copy(
                    isNewOperationReserveValid = reserve.isNotBlank(),
                    newOperationReserve = reserve
                )
            }
        }
    }

    private fun onSaveButtonClick(showDialog: MutableState<Boolean>) {
        if (validateFields()) {
            val operations = uiState.value.operations.toMutableList()
            val newOperation: Operation

            with(uiState.value) {
                newOperation = Operation(
                    description = newOperationDescription,
                    value = formatString(newOperationValue),
                    type = stringProvider.getString(
                        newOperationType?.stringId ?: R.string.operation_type_other
                    ),
                    date = newOperationDate,
                    extras = newOperationOriginAccount.ifBlank { newOperationDestinationAccount }
                )
            }

            with(operations) {
                add(newOperation)
                sortByDescending { it.date }
            }

            uiState.value = uiState.value.copy(operations = operations)
            resetDialogFields()
            showDialog.value = false
        }
    }

    @VisibleForTesting
    fun validateFields(): Boolean {
        updateCommonFieldsValidity()
        updateSpecificFieldsValidity()
        updateDialogFields(resetFields = false)

        return uiState.value.run {
            when {
                newOperationType == null -> false
                else -> isNewOperationDescriptionValid &&
                        isNewOperationValueValid &&
                        isNewOperationTypeValid &&
                        isNewOperationDateValid &&
                        isNewOperationOriginAccountValid &&
                        isNewOperationDestinationAccountValid &&
                        isNewOperationReserveValid
            }
        }
    }

    private fun updateCommonFieldsValidity() = uiState.update {
        it.copy(
            isNewOperationDescriptionValid = it.newOperationDescription.isNotBlank(),
            isNewOperationValueValid = canBeFormatted(it.newOperationValue),
            isNewOperationTypeValid = it.newOperationType != null
        )
    }

    private fun updateSpecificFieldsValidity() = uiState.update { currentState ->
        currentState.copy(
            isNewOperationOriginAccountValid =
                if (currentState.newOperationType?.hasOriginAccount == true) {
                    currentState.newOperationOriginAccount.isNotBlank()
                } else true,
            isNewOperationDestinationAccountValid =
                if (currentState.newOperationType?.hasDestinationAccount == true) {
                    currentState.newOperationDestinationAccount.isNotBlank()
                } else true,
            isNewOperationReserveValid =
                if (currentState.newOperationType?.hasReserve == true) {
                    currentState.newOperationReserve.isNotBlank()
                } else true
        )
    }

    private fun resetDialogFields() {
        uiState.update {
            OperationsScreenContract.UiState().copy(
                operations = it.operations,
                currentDateFilter = it.currentDateFilter,
                searchQuery = it.searchQuery,
                totalBalance = it.totalBalance,
                totalIncome = it.totalIncome,
                totalOutcome = it.totalOutcome
            )
        }
    }

    private fun updateDialogFields(resetFields: Boolean) {
        val accounts = listOf(
            "Conta A",
            "Conta B",
            "Conta C",
            "Conta D",
            "Conta E",
            "Conta F",
            "Conta G",
        )
        val reserves = listOf(
            "Reserva A",
            "Reserva B",
            "Reserva C",
            "Reserva D",
            "Reserva E",
        )
        val newOperationDialogFields = mutableListOf<@Composable () -> Unit>()

        if (resetFields) {
            uiState.update {
                it.copy(
                    newOperationOriginAccount = OperationsScreenUiState.Companion.DEFAULT_STRING_VALUE,
                    newOperationDestinationAccount = OperationsScreenUiState.Companion.DEFAULT_STRING_VALUE,
                    newOperationReserve = OperationsScreenUiState.Companion.DEFAULT_STRING_VALUE
                )
            }
        }

        with(uiState.value) {
            if (newOperationType?.hasOriginAccount == true) {
                newOperationDialogFields.add {
                    AccountsDropdownMenu(
                        operationAccountType = OperationOriginAccount,
                        onAccountSelected = ::onOriginAccountChange,
                        isError = !isNewOperationOriginAccountValid,
                        accounts = accounts
                    )
                }
            }

            if (newOperationType?.hasDestinationAccount == true) {
                newOperationDialogFields.add {
                    AccountsDropdownMenu(
                        operationAccountType = OperationAimedAccount,
                        onAccountSelected = ::onDestinationAccountChange,
                        isError = !isNewOperationDestinationAccountValid,
                        accounts = accounts
                    )
                }
            }

            if (newOperationType?.hasReserve == true) {
                newOperationDialogFields.add {
                    ReservesDropdownMenu(
                        onReserveClicked = ::onReserveChange,
                        isError = !isNewOperationReserveValid,
                        reserves = reserves
                    )
                }
            }
        }

        uiState.update { it.copy(dialogFields = newOperationDialogFields) }
    }

    private fun onSearchQueryChange(query: String) {
        uiState.update { currentState ->
            val filteredList = if (query.isBlank()) {
                uiState.value.operations
            } else {
                uiState.value.operations.filter { operation ->
                    operation.description.contains(query, ignoreCase = true) ||
                            operation.type.contains(query, ignoreCase = true) ||
                            operation.value.contains(query, ignoreCase = true) ||
                            operation.extras?.contains(query, ignoreCase = true) == true
                }
            }
            currentState.copy(operations = filteredList, searchQuery = query)
        }
    }

    private fun onDateFilterChange(localDate: LocalDate) = uiState.update {
        it.copy(currentDateFilter = localDate)
    }

    companion object {
        const val TOTAL_BALANCE_MOCK = 1000.0
        const val TOTAL_INCOME_MOCK = 1500.0
        const val TOTAL_OUTCOME_MOCK = 500.0

    }
}
