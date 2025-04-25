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

package br.com.rbrthmn.ui.financialcompanion.screens.home.components.balancecard

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

interface BalanceCardContract {
    abstract class BalanceCardViewModel : ViewModel() {
        protected val actions = MutableSharedFlow<BalanceCardIntent>()
        abstract val uiState: StateFlow<BalanceCardUiState>
        fun onIntent(intent: BalanceCardIntent) {
            viewModelScope.launch {
                actions.emit(intent)
            }
        }

        protected abstract fun handleIntent()
        abstract fun doOnInit(): BalanceCardViewModel
    }

    sealed class BalanceCardIntent {
        data class OnInitialBalanceChange(val balance: String) : BalanceCardIntent()
        data class OnDescriptionChange(val description: String) : BalanceCardIntent()
        data class OnBankChange(val bankId: Int, val bankName: String) : BalanceCardIntent()
        data class OnSaveClick(val showDialog: MutableState<Boolean>) : BalanceCardIntent()
        data object CleanNewAccount : BalanceCardIntent()
        data class OnDateFilterChange(val date: LocalDate) : BalanceCardIntent()
    }
}
