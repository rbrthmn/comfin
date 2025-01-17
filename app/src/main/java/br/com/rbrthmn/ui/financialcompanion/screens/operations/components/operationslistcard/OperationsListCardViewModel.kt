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

package br.com.rbrthmn.ui.financialcompanion.screens.operations.components.operationslistcard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.com.rbrthmn.model.OperationType
import br.com.rbrthmn.ui.financialcompanion.common.ReservesDropdownMenu
import br.com.rbrthmn.ui.financialcompanion.screens.operations.components.AccountsDropdownMenu
import br.com.rbrthmn.ui.financialcompanion.screens.operations.components.AimedAccount
import br.com.rbrthmn.ui.financialcompanion.screens.operations.components.OriginAccount
import br.com.rbrthmn.ui.financialcompanion.utils.getOperationsMock
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date

class OperationsListCardViewModel : OperationsListCardContract.OperationsListCardViewModel() {
    override var uiState = MutableStateFlow(OperationsListCardUiState())
    override var newOperationDescription: String by mutableStateOf("")
    override var isNewOperationDescriptionValid: Boolean by mutableStateOf(true)
    override var newOperationValue: String by mutableStateOf("")
    override var isNewOperationValueValid: Boolean by mutableStateOf(true)
    override var newOperationType: OperationType by mutableStateOf(OperationType.DEPOSIT)
    override var isNewOperationTypeValid: Boolean by mutableStateOf(true)
    override var newOriginAccount: String by mutableStateOf("")
    override var isNewOriginAccountValid: Boolean by mutableStateOf(true)
    override var newDestinationAccount: String by mutableStateOf("")
    override var isNewDestinationAccountValid: Boolean by mutableStateOf(true)
    override var newOperationDate: Date by mutableStateOf(Date())
    override var newReserve: String by mutableStateOf("")
    override var isNewReserveValid: Boolean by mutableStateOf(true)

    init {
        uiState.value =
            OperationsListCardUiState(operations = getOperationsMock().sortedByDescending { it.date })
        updateDialogFields()
    }

    override fun onDescriptionChange(description: String) {
        newOperationDescription = description
    }

    override fun onValueChange(value: String) {
        newOperationValue = value
    }

    override fun onOperationTypeChange(operationType: OperationType) {
        newOperationType = operationType
        updateDialogFields()
    }

    override fun onOriginAccountChange(originAccount: String) {
        newOriginAccount = originAccount
    }

    override fun onDestinationAccountChange(destinationAccount: String) {
       newDestinationAccount = destinationAccount
    }

    override fun onOperationDateChange(operationDate: Long?) {
        operationDate?.let {
            newOperationDate = Date(it)
        }
    }

    override fun onReserveChange(reserve: String) {
        newReserve = reserve
    }

    override fun onSaveButtonClick() {
    }

    private fun updateDialogFields() {
        val newDialogFields = mutableListOf<@Composable () -> Unit>()
        when (newOperationType) {
            OperationType.TRANSFER -> {
                newDialogFields.add { AccountsDropdownMenu(accountType = OriginAccount) }
                newDialogFields.add { AccountsDropdownMenu(accountType = AimedAccount) }
            }

            OperationType.DEBIT_PURCHASE,
            OperationType.BILL_PAYMENT,
            OperationType.WITHDRAWAL -> {
                newDialogFields.add { AccountsDropdownMenu(accountType = OriginAccount) }
            }

            OperationType.DEPOSIT,
            OperationType.INCOME -> {
                newDialogFields.add { AccountsDropdownMenu(accountType = AimedAccount) }
            }

            OperationType.RESERVE_ALLOCATION -> {
                newDialogFields.add { AccountsDropdownMenu(accountType = OriginAccount) }
                newDialogFields.add { ReservesDropdownMenu(onReserveClicked = ::onReserveChange) }
            }

            OperationType.RESERVE_WITHDRAWAL -> {
                newDialogFields.add { AccountsDropdownMenu(accountType = AimedAccount) }
                newDialogFields.add { ReservesDropdownMenu(onReserveClicked = ::onReserveChange) }
            }

            OperationType.OTHER -> {
                newDialogFields.add { AccountsDropdownMenu(accountType = OriginAccount) }
            }
        }
        uiState.value = uiState.value.copy(dialogFields = newDialogFields)
    }
}