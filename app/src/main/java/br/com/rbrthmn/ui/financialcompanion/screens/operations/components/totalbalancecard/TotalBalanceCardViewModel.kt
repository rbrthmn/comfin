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

package br.com.rbrthmn.ui.financialcompanion.screens.operations.components.totalbalancecard

import br.com.rbrthmn.ui.financialcompanion.utils.formatDouble
import kotlinx.coroutines.flow.MutableStateFlow

class TotalBalanceCardViewModel  : TotalBalanceCardContract.TotalBalanceCardViewModel() {
    override var uiState = MutableStateFlow(TotalBalanceCardUiState())
        private set

    init {
        uiState.value = TotalBalanceCardUiState(
            totalBalance = formatDouble(TOTAL_BALANCE_MOCK),
            totalIncome = formatDouble(TOTAL_INCOME_MOCK),
            totalOutcome = formatDouble(TOTAL_OUTCOME_MOCK)
        )
    }

    private companion object {
        const val TOTAL_BALANCE_MOCK = 1000.0
        const val TOTAL_INCOME_MOCK = 1500.0
        const val TOTAL_OUTCOME_MOCK = 500.0

    }
}