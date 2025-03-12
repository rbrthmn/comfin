/*
 *
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
 *
 */

package br.com.rbrthmn.ui.financialcompanion.screens.operations

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import br.com.rbrthmn.R
import br.com.rbrthmn.model.OperationType
import br.com.rbrthmn.ui.financialcompanion.components.ReservesDropdownMenu
import br.com.rbrthmn.ui.financialcompanion.screens.operations.OperationsScreenUiState.Companion.DEFAULT_STRING_VALUE
import br.com.rbrthmn.ui.financialcompanion.screens.operations.components.AccountsDropdownMenu
import br.com.rbrthmn.ui.financialcompanion.utils.StringProvider
import br.com.rbrthmn.ui.financialcompanion.utils.canBeFormatted
import br.com.rbrthmn.ui.financialcompanion.utils.formatDouble
import br.com.rbrthmn.ui.financialcompanion.utils.formatString
import br.com.rbrthmn.ui.financialcompanion.utils.getOperationsMock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class OperationsScreenViewModel(val stringProvider: StringProvider) :
    OperationsScreenContract.OperationsScreenViewModel() {
    override var uiState = MutableStateFlow(OperationsScreenUiState())

    override fun doOnInit(): OperationsScreenViewModel {
        uiState.value =
            OperationsScreenUiState(
                operations = getOperationsMock().sortedByDescending { it.date },
                totalBalance = formatDouble(TOTAL_BALANCE_MOCK),
                totalIncome = formatDouble(TOTAL_INCOME_MOCK),
                totalOutcome = formatDouble(TOTAL_OUTCOME_MOCK)
            )
        updateDialogFields(resetFields = true)

        return this
    }

    override fun onDescriptionChange(description: String) = uiState.update {
        it.copy(
            newOperationDescription = description,
            isNewOperationDescriptionValid = description.isNotBlank()
        )
    }

    override fun onValueChange(value: String) = uiState.update {
        it.copy(
            newOperationValue = value,
            isNewOperationValueValid = canBeFormatted(value)
        )
    }

    override fun onOperationTypeChange(operationType: OperationType) {
        uiState.update {
            it.copy(
                newOperationType = operationType,
                isNewOperationTypeValid = true
            )
        }
        updateDialogFields(resetFields = true)
    }

    override fun onOriginAccountChange(originAccount: String) {
        if (uiState.value.newOperationType?.hasOriginAccount == true) {
            uiState.update {
                it.copy(
                    isNewOperationOriginAccountValid = originAccount.isNotBlank(),
                    newOperationOriginAccount = originAccount
                )
            }
        }
    }

    override fun onDestinationAccountChange(destinationAccount: String) {
        if (uiState.value.newOperationType?.hasDestinationAccount == true) {
            uiState.update {
                it.copy(
                    isNewOperationDestinationAccountValid = destinationAccount.isNotBlank(),
                    newOperationDestinationAccount = destinationAccount
                )
            }
        }
    }

    override fun onOperationDateChange(operationDate: LocalDate) = uiState.update {
        it.copy(
            newOperationDate = operationDate,
            isNewOperationDateValid = true
        )
    }

    override fun onReserveChange(reserve: String) {
        if (uiState.value.newOperationType?.hasReserve == true) {
            uiState.update {
                it.copy(
                    isNewOperationReserveValid = reserve.isNotBlank(),
                    newOperationReserve = reserve
                )
            }
        }
    }

    override fun onSaveButtonClick(showDialog: MutableState<Boolean>) {
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

    override fun resetDialogFields() {
        uiState.update {
            OperationsScreenUiState().copy(
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
        val newOperationDialogFields = mutableListOf<@Composable () -> Unit>()
        if (resetFields) {
            uiState.update {
                it.copy(
                    newOperationOriginAccount = DEFAULT_STRING_VALUE,
                    newOperationDestinationAccount = DEFAULT_STRING_VALUE,
                    newOperationReserve = DEFAULT_STRING_VALUE
                )
            }
        }

        with(uiState.value) {
            if (newOperationType?.hasOriginAccount == true) {
                newOperationDialogFields.add {
                    AccountsDropdownMenu(
                        operationAccountType = OperationOriginAccount,
                        onAccountSelected = ::onOriginAccountChange,
                        isError = !isNewOperationOriginAccountValid
                    )
                }
            }

            if (newOperationType?.hasDestinationAccount == true) {
                newOperationDialogFields.add {
                    AccountsDropdownMenu(
                        operationAccountType = OperationAimedAccount,
                        onAccountSelected = ::onDestinationAccountChange,
                        isError = !isNewOperationDestinationAccountValid
                    )
                }
            }

            if (newOperationType?.hasReserve == true) {
                newOperationDialogFields.add {
                    ReservesDropdownMenu(
                        onReserveClicked = ::onReserveChange,
                        isError = !isNewOperationReserveValid
                    )
                }
            }
        }

        uiState.update { it.copy(dialogFields = newOperationDialogFields) }
    }

    override fun onSearchQueryChange(query: String) {
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

    override fun onDateFilterChange(localDate: LocalDate) = uiState.update {
        it.copy(currentDateFilter = localDate)
    }

    companion object {
        const val TOTAL_BALANCE_MOCK = 1000.0
        const val TOTAL_INCOME_MOCK = 1500.0
        const val TOTAL_OUTCOME_MOCK = 500.0

    }
}
