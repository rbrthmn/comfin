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

import br.com.rbrthmn.ui.financialcompanion.screens.home.components.lastmonthdifferencecard.LastMonthDifferenceCardViewModel
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.lastmonthdifferencecard.LastMonthDifferenceCardViewModel.Companion.MOCK
import br.com.rbrthmn.ui.financialcompanion.utils.formatDouble
import junit.framework.TestCase
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class LastMonthDifferenceCardViewModelTest {
    private val viewModel = LastMonthDifferenceCardViewModel()

    @Test
    fun `doOnInit should assign initial values`() {
        viewModel.doOnInit()

        assertEquals(formatDouble(MOCK), viewModel.uiState.value.valueOfLastMonth)
    }

    @Test
    fun `setDateFilter should assign value correctly`() {
        viewModel.setDateFilter(VALID_DATE)

        TestCase.assertEquals(
            VALID_DATE,
            viewModel.uiState.value.currentDateFilter
        )
    }


    private companion object {
        val VALID_DATE: LocalDate = LocalDate.of(1998, 10, 20)
    }
}