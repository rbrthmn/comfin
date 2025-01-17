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

import androidx.lifecycle.ViewModel
import br.com.rbrthmn.model.OperationType
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date

interface OperationsListCardContract {
    abstract class OperationsListCardViewModel : ViewModel() {
        abstract val uiState: MutableStateFlow<OperationsListCardUiState>
        abstract val newOperationDescription: String
        abstract val isNewOperationDescriptionValid: Boolean
        abstract val newOperationValue: String
        abstract val isNewOperationValueValid: Boolean
        abstract val newOperationType: OperationType
        abstract val isNewOperationTypeValid: Boolean
        abstract val newOriginAccount: String
        abstract val isNewOriginAccountValid: Boolean
        abstract val newDestinationAccount: String
        abstract val isNewDestinationAccountValid: Boolean
        abstract val newOperationDate: Date
        abstract val newReserve: String
        abstract val isNewReserveValid: Boolean
        abstract fun onDescriptionChange(description: String)
        abstract fun onValueChange(value: String)
        abstract fun onOperationTypeChange(operationType: OperationType)
        abstract fun onOriginAccountChange(originAccount: String)
        abstract fun onDestinationAccountChange(destinationAccount: String)
        abstract fun onOperationDateChange(operationDate: Long?)
        abstract fun onReserveChange(reserve: String)
        abstract fun onSaveButtonClick()
    }
}