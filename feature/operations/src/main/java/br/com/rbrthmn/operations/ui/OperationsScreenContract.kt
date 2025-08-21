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

package br.com.rbrthmn.operations.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import br.com.rbrthmn.ui.BaseViewModel
import java.time.LocalDate

interface OperationsScreenContract {
    abstract class ViewModel : BaseViewModel<UiState, Intent>()

    data class UiState(
        val operations: List<Operation> = listOf(),
        val dialogFields: MutableList<@Composable () -> Unit> = mutableListOf(),
        val totalBalance: String = DEFAULT_STRING_VALUE,
        val totalIncome: String = DEFAULT_STRING_VALUE,
        val totalOutcome: String = DEFAULT_STRING_VALUE,
        val newOperationDescription: String = DEFAULT_STRING_VALUE,
        val isNewOperationDescriptionValid: Boolean = true,
        val newOperationValue: String = DEFAULT_STRING_VALUE,
        val isNewOperationValueValid: Boolean = true,
        val newOperationType: OperationType? = null,
        val isNewOperationTypeValid: Boolean = true,
        val newOperationOriginAccount: String = DEFAULT_STRING_VALUE,
        val isNewOperationOriginAccountValid: Boolean = true,
        val newOperationDestinationAccount: String = DEFAULT_STRING_VALUE,
        val isNewOperationDestinationAccountValid: Boolean = true,
        val newOperationDate: LocalDate = LocalDate.now(),
        var isNewOperationDateValid: Boolean = true,
        val newOperationReserve: String = DEFAULT_STRING_VALUE,
        val isNewOperationReserveValid: Boolean = true,
        val searchQuery: String = DEFAULT_STRING_VALUE,
        val currentDateFilter: LocalDate = LocalDate.now()
    ) {
        companion object {
            const val DEFAULT_STRING_VALUE = ""
        }

    }

    sealed class Intent {
        data class OnDescriptionChange(val description: String) : Intent()
        data class OnValueChange(val value: String) : Intent()
        data class OnOperationTypeChange(val operationType: OperationType) : Intent()
        data class OnOriginAccountChange(val originAccount: String) : Intent()
        data class OnDestinationAccountChange(val destinationAccount: String) : Intent()
        data class OnOperationDateChange(val operationDate: LocalDate) : Intent()
        data class OnReserveChange(val reserve: String) : Intent()
        data class OnSaveButtonClick(val showDialog: MutableState<Boolean>) : Intent()
        object OnResetDialogFields : Intent()
        data class OnSearchQueryChange(val query: String) : Intent()
        data class OnDateFilterChange(val localDate: LocalDate) : Intent()
    }
}

data class Operation(
    val extras: String? = null,
    val description: String,
    val value: String,
    val date: LocalDate,
    val type: String
)
