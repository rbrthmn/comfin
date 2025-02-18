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
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class LastMonthDifferenceCardViewModelTest {
    private val viewModel = LastMonthDifferenceCardViewModel()

    @Test
    fun `init should assign ui state value correctly`() = runBlocking {
        val uiState = viewModel.uiState.first()

        assertEquals(EXPECTED_FIRST_VALUE, uiState.valueOfLastMonth)
    }

    @Test
    fun `setDateFilter should assign value correctly`() {
        viewModel.setDateFilter(VALID_DATE)

        TestCase.assertEquals(
            VALID_DATE,
            viewModel.currentDateFilter
        )
    }


    private companion object {
        const val EXPECTED_FIRST_VALUE = "-100.00"
        val VALID_DATE: LocalDate = LocalDate.of(1998, 10, 20)
    }
}