/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.rbrthmn.ui.financialcompanion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import br.com.rbrthmn.data.FinancialCompanionRepository
import br.com.rbrthmn.ui.financialcompanion.FinancialCompanionUiState.Error
import br.com.rbrthmn.ui.financialcompanion.FinancialCompanionUiState.Loading
import br.com.rbrthmn.ui.financialcompanion.FinancialCompanionUiState.Success
import javax.inject.Inject

@HiltViewModel
class FinancialCompanionViewModel @Inject constructor(
    private val financialCompanionRepository: FinancialCompanionRepository
) : ViewModel() {

    val uiState: StateFlow<FinancialCompanionUiState> = financialCompanionRepository
        .financialCompanions.map<List<String>, FinancialCompanionUiState>(::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addFinancialCompanion(name: String) {
        viewModelScope.launch {
            financialCompanionRepository.add(name)
        }
    }
}

sealed interface FinancialCompanionUiState {
    object Loading : FinancialCompanionUiState
    data class Error(val throwable: Throwable) : FinancialCompanionUiState
    data class Success(val data: List<String>) : FinancialCompanionUiState
}
