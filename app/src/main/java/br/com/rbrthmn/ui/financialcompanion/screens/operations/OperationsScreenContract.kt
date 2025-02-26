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
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

interface OperationsScreenContract {
    abstract class OperationsScreenViewModel : ViewModel() {
        abstract val uiState: StateFlow<OperationsScreenUiState>
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
        abstract fun doOnInit(): br.com.rbrthmn.ui.financialcompanion.screens.operations.OperationsScreenViewModel
    }
}
