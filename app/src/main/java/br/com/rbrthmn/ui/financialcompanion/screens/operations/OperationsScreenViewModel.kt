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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.com.rbrthmn.R
import br.com.rbrthmn.model.OperationType
import br.com.rbrthmn.ui.financialcompanion.common.ReservesDropdownMenu
import br.com.rbrthmn.ui.financialcompanion.screens.operations.components.AccountsDropdownMenu
import br.com.rbrthmn.ui.financialcompanion.screens.operations.components.OperationAimedAccount
import br.com.rbrthmn.ui.financialcompanion.screens.operations.components.OperationOriginAccount
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
    override var newOperationDescription: String by mutableStateOf("")
    override var isNewOperationDescriptionValid: Boolean by mutableStateOf(true)
    override var newOperationValue: String by mutableStateOf("")
    override var isNewOperationValueValid: Boolean by mutableStateOf(true)
    override var newOperationType: OperationType? by mutableStateOf(null)
    override var isNewOperationTypeValid: Boolean by mutableStateOf(true)
    override var newOperationOriginAccount: String by mutableStateOf("")
    override var isNewOperationOriginAccountValid: Boolean by mutableStateOf(true)
    override var newOperationDestinationAccount: String by mutableStateOf("")
    override var isNewOperationDestinationAccountValid: Boolean by mutableStateOf(true)
    override var newOperationDate: LocalDate by mutableStateOf(LocalDate.now())
    override var isNewOperationDateValid: Boolean by mutableStateOf(true)
    override var newOperationReserve: String by mutableStateOf("")
    override var isNewOperationReserveValid: Boolean by mutableStateOf(true)
    override var searchQuery: String by mutableStateOf("")
    override var currentDate: LocalDate by mutableStateOf(LocalDate.now())

    init {
        uiState.value =
            OperationsScreenUiState(
                operations = getOperationsMock().sortedByDescending { it.date },
                totalBalance = formatDouble(TOTAL_BALANCE_MOCK),
                totalIncome = formatDouble(TOTAL_INCOME_MOCK),
                totalOutcome = formatDouble(TOTAL_OUTCOME_MOCK)
            )
        updateDialogFields()
    }

    override fun onDescriptionChange(description: String) {
        isNewOperationDescriptionValid = description.isNotBlank()
        newOperationDescription = description
    }

    override fun onValueChange(value: String) {
        isNewOperationValueValid = canBeFormatted(value)
        newOperationValue = value
    }

    override fun onOperationTypeChange(operationType: OperationType) {
        isNewOperationTypeValid = true
        newOperationType = operationType
        updateDialogFields()
    }

    override fun onOriginAccountChange(originAccount: String) {
        if (newOperationType == OperationType.TRANSFER ||
            newOperationType == OperationType.DEBIT_PURCHASE ||
            newOperationType == OperationType.BILL_PAYMENT ||
            newOperationType == OperationType.WITHDRAWAL ||
            newOperationType == OperationType.RESERVE_ALLOCATION ||
            newOperationType == OperationType.OTHER
        ) {
            isNewOperationOriginAccountValid = originAccount.isNotBlank()
            newOperationOriginAccount = originAccount
        }
    }

    override fun onDestinationAccountChange(destinationAccount: String) {
        if (newOperationType == OperationType.TRANSFER ||
            newOperationType == OperationType.DEPOSIT ||
            newOperationType == OperationType.INCOME ||
            newOperationType == OperationType.RESERVE_WITHDRAWAL
        ) {
            isNewOperationDestinationAccountValid = destinationAccount.isNotBlank()
            newOperationDestinationAccount = destinationAccount
        }
    }

    override fun onOperationDateChange(operationDate: LocalDate) {
        newOperationDate = operationDate
        isNewOperationDateValid = true
    }

    override fun onReserveChange(reserve: String) {
        if (newOperationType == OperationType.RESERVE_ALLOCATION ||
            newOperationType == OperationType.RESERVE_WITHDRAWAL
        ) {
            isNewOperationReserveValid = reserve.isNotBlank()
            newOperationReserve = reserve
        }
    }

    override fun onSaveButtonClick(showDialog: MutableState<Boolean>) {
        if (validateFields()) {
            val newOperation = Operation(
                description = newOperationDescription,
                value = formatString(newOperationValue),
                type = stringProvider.getString(
                    newOperationType?.stringId ?: R.string.operation_type_other
                ),
                date = newOperationDate,
                extras = newOperationOriginAccount.ifBlank { newOperationDestinationAccount }
            )

            val newOperations = uiState.value.operations.toMutableList()
            with(newOperations) {
                add(newOperation)
                sortByDescending { it.date }
            }
            uiState.value = uiState.value.copy(operations = newOperations)

            resetDialogFields()
            showDialog.value = false
        }
    }

    @VisibleForTesting
    fun validateFields(): Boolean {
        if (newOperationType == null) validateCommonFields()

        return when (newOperationType) {
            OperationType.TRANSFER -> {
                validateCommonFields()
                isNewOperationOriginAccountValid = newOperationOriginAccount.isNotBlank()
                isNewOperationDestinationAccountValid =
                    newOperationDestinationAccount.isNotBlank()
                isNewOperationOriginAccountValid && isNewOperationDestinationAccountValid
            }

            OperationType.DEBIT_PURCHASE,
            OperationType.BILL_PAYMENT,
            OperationType.WITHDRAWAL -> {
                validateCommonFields()
                isNewOperationOriginAccountValid = newOperationOriginAccount.isNotBlank()
                isNewOperationOriginAccountValid
            }

            OperationType.DEPOSIT,
            OperationType.INCOME -> {
                validateCommonFields()
                isNewOperationDestinationAccountValid =
                    newOperationDestinationAccount.isNotBlank()
                isNewOperationDestinationAccountValid
            }

            OperationType.RESERVE_ALLOCATION -> {
                validateCommonFields()
                isNewOperationReserveValid = newOperationReserve.isNotBlank()
                isNewOperationOriginAccountValid = newOperationOriginAccount.isNotBlank()
                isNewOperationReserveValid && isNewOperationOriginAccountValid
            }

            OperationType.RESERVE_WITHDRAWAL -> {
                validateCommonFields()
                isNewOperationReserveValid = newOperationReserve.isNotBlank()
                isNewOperationDestinationAccountValid =
                    newOperationDestinationAccount.isNotBlank()
                isNewOperationReserveValid && isNewOperationDestinationAccountValid
            }

            OperationType.OTHER -> {
                validateCommonFields()
                isNewOperationOriginAccountValid = newOperationOriginAccount.isNotBlank()
                isNewOperationOriginAccountValid
            }

            null -> false
        } && isNewOperationDescriptionValid &&
                isNewOperationValueValid &&
                isNewOperationDateValid
    }

    private fun validateCommonFields() {
        isNewOperationDescriptionValid = newOperationDescription.isNotBlank()
        isNewOperationValueValid = canBeFormatted(newOperationValue)
        isNewOperationTypeValid = newOperationType != null
    }

    override fun resetDialogFields() {
        newOperationDescription = ""
        isNewOperationDescriptionValid = true
        newOperationValue = ""
        isNewOperationValueValid = true
        newOperationType = null
        isNewOperationTypeValid = true
        newOperationOriginAccount = ""
        isNewOperationOriginAccountValid = true
        newOperationDestinationAccount = ""
        isNewOperationDestinationAccountValid = true
        newOperationDate = LocalDate.now()
        isNewOperationDateValid = true
        newOperationReserve = ""
        isNewOperationReserveValid = true
        uiState.value = uiState.value.copy(dialogFields = mutableListOf())
    }

    private fun updateDialogFields() {
        val newOperationDialogFields = mutableListOf<@Composable () -> Unit>()
        newOperationOriginAccount = ""
        newOperationDestinationAccount = ""
        newOperationReserve = ""

        when (newOperationType) {
            OperationType.TRANSFER -> {
                newOperationDialogFields.add {
                    AccountsDropdownMenu(
                        operationAccountType = OperationOriginAccount,
                        onAccountSelected = ::onOriginAccountChange,
                        isError = !isNewOperationOriginAccountValid
                    )
                }
                newOperationDialogFields.add {
                    AccountsDropdownMenu(
                        operationAccountType = OperationAimedAccount,
                        onAccountSelected = ::onDestinationAccountChange,
                        isError = !isNewOperationDestinationAccountValid
                    )
                }
            }

            OperationType.DEBIT_PURCHASE,
            OperationType.BILL_PAYMENT,
            OperationType.WITHDRAWAL -> {
                newOperationDialogFields.add {
                    AccountsDropdownMenu(
                        operationAccountType = OperationOriginAccount,
                        onAccountSelected = ::onOriginAccountChange,
                        isError = !isNewOperationOriginAccountValid
                    )
                }
            }

            OperationType.DEPOSIT,
            OperationType.INCOME -> {
                newOperationDialogFields.add {
                    AccountsDropdownMenu(
                        operationAccountType = OperationAimedAccount,
                        onAccountSelected = ::onDestinationAccountChange,
                        isError = !isNewOperationDestinationAccountValid
                    )
                }
            }

            OperationType.RESERVE_ALLOCATION -> {
                newOperationDialogFields.add {
                    AccountsDropdownMenu(
                        operationAccountType = OperationOriginAccount,
                        onAccountSelected = ::onOriginAccountChange,
                        isError = !isNewOperationOriginAccountValid
                    )
                }
                newOperationDialogFields.add {
                    ReservesDropdownMenu(
                        onReserveClicked = ::onReserveChange,
                        isError = !isNewOperationReserveValid
                    )
                }
            }

            OperationType.RESERVE_WITHDRAWAL -> {
                newOperationDialogFields.add {
                    AccountsDropdownMenu(
                        operationAccountType = OperationAimedAccount,
                        onAccountSelected = ::onDestinationAccountChange,
                        isError = !isNewOperationDestinationAccountValid
                    )
                }
                newOperationDialogFields.add {
                    ReservesDropdownMenu(
                        onReserveClicked = ::onReserveChange,
                        isError = !isNewOperationReserveValid
                    )
                }
            }

            OperationType.OTHER -> {
                newOperationDialogFields.add {
                    AccountsDropdownMenu(
                        operationAccountType = OperationOriginAccount,
                        onAccountSelected = ::onOriginAccountChange,
                        isError = !isNewOperationOriginAccountValid
                    )
                }
            }

            null -> Unit
        }
        uiState.value = uiState.value.copy(dialogFields = newOperationDialogFields)
    }

    override fun onSearchQueryChange(query: String) {
        searchQuery = query
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
            currentState.copy(operations = filteredList)
        }
    }

    override fun onDateFilterChange(localDate: LocalDate) {
        currentDate = localDate
    }

    private companion object {
        const val TOTAL_BALANCE_MOCK = 1000.0
        const val TOTAL_INCOME_MOCK = 1500.0
        const val TOTAL_OUTCOME_MOCK = 500.0

    }
}
