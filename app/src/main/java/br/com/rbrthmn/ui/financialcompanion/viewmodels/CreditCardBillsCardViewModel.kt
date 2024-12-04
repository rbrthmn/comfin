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

package br.com.rbrthmn.ui.financialcompanion.viewmodels

import br.com.rbrthmn.contracts.CreditCardContract
import br.com.rbrthmn.ui.financialcompanion.uistates.CreditCardsBillCardUiState
import br.com.rbrthmn.ui.financialcompanion.uistates.FinancialOverviewUiState
import br.com.rbrthmn.ui.financialcompanion.utils.formatDouble
import kotlinx.coroutines.flow.MutableStateFlow

class CreditCardBillsCardViewModel : CreditCardContract.CreditCardsBillCardViewModel() {
    override var uiState = MutableStateFlow(CreditCardsBillCardUiState())
        private set

    init {
        val bills = listOf(
            FinancialOverviewUiState("Cartao A", formatDouble(5000.00), "R$"),
            FinancialOverviewUiState("Cartao B", formatDouble(10000.00), "R$"),
            FinancialOverviewUiState("Cartao C", formatDouble(5.00), "R$")
        )
        val totalBill = 1200.00

        uiState.value =
            CreditCardsBillCardUiState(totalBill = formatDouble(totalBill), bills = bills)
    }
}