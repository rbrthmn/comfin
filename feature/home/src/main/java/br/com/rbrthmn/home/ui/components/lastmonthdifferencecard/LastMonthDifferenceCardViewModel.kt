/*
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
 */

package br.com.rbrthmn.home.ui.components.lastmonthdifferencecard

import br.com.rbrthmn.ui.utils.formatDouble
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class LastMonthDifferenceCardViewModel : LastMonthDifferenceCardContract.ViewModel() {
    override var uiState = MutableStateFlow(LastMonthDifferenceCardContract.UiState())

    override fun doOnInit(): LastMonthDifferenceCardViewModel {
        uiState.value = LastMonthDifferenceCardContract.UiState(formatDouble(MOCK))

        return this
    }

    override fun onIntent(intent: LastMonthDifferenceCardContract.Intent) {
        when (intent) {
            is LastMonthDifferenceCardContract.Intent.OnDateFilterChange -> setDateFilter(intent.date)
        }
    }

    private fun setDateFilter(date: LocalDate) = uiState.update {
        it.copy(currentDateFilter = date)
    }

    companion object {
        const val MOCK = -221.89
    }
}