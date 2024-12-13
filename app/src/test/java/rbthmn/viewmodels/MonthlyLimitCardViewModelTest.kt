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

package rbthmn.viewmodels

import br.com.rbrthmn.ui.financialcompanion.screens.home.components.monthlylimitcard.MonthlyLimitCardViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test

class MonthlyLimitCardViewModelTest {
    @Test
    fun uiState_initialValue_isCorrect() = runBlocking {
        val viewModel = MonthlyLimitCardViewModel()
        val uiState = viewModel.uiState.first()

        assertEquals("1,000.00", uiState.monthLimit)
        assertEquals("500.00", uiState.monthDifference)
    }
}