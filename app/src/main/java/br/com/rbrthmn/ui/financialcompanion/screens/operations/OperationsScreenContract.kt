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

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import br.com.rbrthmn.model.OperationType
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

interface OperationsScreenContract {
    abstract class OperationsScreenViewModel : ViewModel() {
        abstract val uiState: MutableStateFlow<OperationsScreenUiState>
        abstract val newOperationDescription: String
        abstract val isNewOperationDescriptionValid: Boolean
        abstract val newOperationValue: String
        abstract val isNewOperationValueValid: Boolean
        abstract val newOperationType: OperationType?
        abstract val isNewOperationTypeValid: Boolean
        abstract val newOperationOriginAccount: String
        abstract val isNewOperationOriginAccountValid: Boolean
        abstract val newOperationDestinationAccount: String
        abstract val isNewOperationDestinationAccountValid: Boolean
        abstract val newOperationDate: LocalDate
        abstract var isNewOperationDateValid: Boolean
        abstract val newOperationReserve: String
        abstract val isNewOperationReserveValid: Boolean
        abstract val searchQuery: String
        abstract val currentDate: LocalDate
        abstract fun onDescriptionChange(description: String)
        abstract fun onValueChange(value: String)
        abstract fun onOperationTypeChange(operationType: OperationType)
        abstract fun onOriginAccountChange(originAccount: String)
        abstract fun onDestinationAccountChange(destinationAccount: String)
        abstract fun onOperationDateChange(operationDate: LocalDate)
        abstract fun onReserveChange(reserve: String)
        abstract fun onSaveButtonClick(showDialog: MutableState<Boolean>)
        abstract fun resetDialogFields()
        abstract fun onSearchQueryChange(query: String)
        abstract fun onDateFilterChange(localDate: LocalDate)
    }
}
