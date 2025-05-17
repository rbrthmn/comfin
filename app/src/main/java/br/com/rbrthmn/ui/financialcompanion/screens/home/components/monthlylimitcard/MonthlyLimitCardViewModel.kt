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

package br.com.rbrthmn.ui.financialcompanion.screens.home.components.monthlylimitcard

import br.com.rbrthmn.ui.financialcompanion.utils.formatDouble
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class MonthlyLimitCardViewModel : MonthlyLimitCardContract.ViewModel() {
    override var uiState = MutableStateFlow(MonthlyLimitCardContract.UiState())

    override fun doOnInit(): MonthlyLimitCardViewModel {
        uiState.value = MonthlyLimitCardContract.UiState(
            monthLimit = formatDouble(MONTH_LIMIT_MOCK),
            monthDifference = formatDouble(MONTH_DIFFERENCE_MOCK)
        )

        return this
    }

    override fun onIntent(intent: MonthlyLimitCardContract.Intent) {
        when (intent) {
            is MonthlyLimitCardContract.Intent.OnDateFilterChange -> onDateFilterChange(intent.date)
        }
    }

    private fun onDateFilterChange(date: LocalDate) = uiState.update {
        it.copy(currentDateFilter = date)
    }

    companion object {
        const val MONTH_LIMIT_MOCK = 1000.0
        const val MONTH_DIFFERENCE_MOCK = 500.0
    }
}
