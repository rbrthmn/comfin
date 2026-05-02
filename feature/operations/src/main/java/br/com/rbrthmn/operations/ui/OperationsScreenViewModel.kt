/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Modifications made by Roberto Kenzo Hamano, 2024
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.rbrthmn.operations.ui

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import br.com.rbrthmn.data.operations.repository.OperationsRepository
import br.com.rbrthmn.operations.R
import br.com.rbrthmn.operations.ui.components.AccountsDropdownMenu
import br.com.rbrthmn.operations.ui.components.OperationAimedAccount
import br.com.rbrthmn.operations.ui.components.OperationOriginAccount
import br.com.rbrthmn.ui.components.ReservesDropdownMenu
import br.com.rbrthmn.ui.utils.StringProvider
import br.com.rbrthmn.ui.utils.canBeFormatted
import br.com.rbrthmn.ui.utils.formatDouble
import br.com.rbrthmn.ui.utils.formatString
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class OperationsScreenViewModel(
    val stringProvider: StringProvider,
    private val operationsRepository: OperationsRepository
) : OperationsScreenContract.ViewModel() {

    override var uiState = MutableStateFlow(OperationsScreenContract.UiState())
    private var collectJob: Job? = null
    private var allOperations: List<Operation> = emptyList()

    override fun doOnInit(): OperationsScreenViewModel {
        collectTransactions(LocalDate.now())
        updateDialogFields(resetFields = true)
        return this
    }

    override fun onIntent(intent: OperationsScreenContract.Intent) {
        when (intent) {
            is OperationsScreenContract.Intent.OnDescriptionChange -> onDescriptionChange(intent.description)
            is OperationsScreenContract.Intent.OnValueChange -> onValueChange(intent.value)
            is OperationsScreenContract.Intent.OnOperationTypeChange -> onOperationTypeChange(intent.operationType)
            is OperationsScreenContract.Intent.OnOriginAccountChange -> onOriginAccountChange(intent.originAccount)
            is OperationsScreenContract.Intent.OnDestinationAccountChange -> onDestinationAccountChange(intent.destinationAccount)
            is OperationsScreenContract.Intent.OnOperationDateChange -> onOperationDateChange(intent.operationDate)
            is OperationsScreenContract.Intent.OnReserveChange -> onReserveChange(intent.reserve)
            is OperationsScreenContract.Intent.OnSaveButtonClick -> onSaveButtonClick(intent.showDialog)
            is OperationsScreenContract.Intent.OnResetDialogFields -> updateDialogFields(resetFields = true)
            is OperationsScreenContract.Intent.OnSearchQueryChange -> onSearchQueryChange(intent.query)
            is OperationsScreenContract.Intent.OnDateFilterChange -> onDateFilterChange(intent.localDate)
        }
    }

    private fun collectTransactions(date: LocalDate) {
        collectJob?.cancel()
        collectJob = viewModelScope.launch {
            operationsRepository.getOperationsForMonth(date.year, date.monthValue)
                .collect { data ->
                    val operations = data.operations.map { item ->
                        Operation(
                            description = item.counterparty,
                            value = formatDouble(item.amount),
                            type = item.type,
                            date = item.date,
                            extras = item.accountName
                        )
                    }
                    allOperations = operations
                    uiState.update {
                        it.copy(
                            operations = applySearchFilter(operations, it.searchQuery),
                            totalIncome = formatDouble(data.totalIncome),
                            totalOutcome = formatDouble(data.totalOutcome),
                            totalBalance = formatDouble(data.totalBalance)
                        )
                    }
                }
        }
    }

    private fun applySearchFilter(operations: List<Operation>, query: String): List<Operation> =
        if (query.isBlank()) operations
        else operations.filter { op ->
            op.description.contains(query, ignoreCase = true) ||
                    op.type.contains(query, ignoreCase = true) ||
                    op.value.contains(query, ignoreCase = true) ||
                    op.extras?.contains(query, ignoreCase = true) == true
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
                    newOperationOriginAccount = OperationsScreenContract.UiState.DEFAULT_STRING_VALUE,
                    newOperationDestinationAccount = OperationsScreenContract.UiState.DEFAULT_STRING_VALUE,
                    newOperationReserve = OperationsScreenContract.UiState.DEFAULT_STRING_VALUE
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
        uiState.update {
            it.copy(
                operations = applySearchFilter(allOperations, query),
                searchQuery = query
            )
        }
    }

    private fun onDateFilterChange(localDate: LocalDate) {
        uiState.update { it.copy(currentDateFilter = localDate) }
        collectTransactions(localDate)
    }

}
