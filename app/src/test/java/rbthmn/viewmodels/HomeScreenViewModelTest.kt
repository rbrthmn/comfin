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

import br.com.rbrthmn.ui.financialcompanion.screens.home.HomeScreenContract
import br.com.rbrthmn.ui.financialcompanion.screens.home.HomeScreenViewModel
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class HomeScreenViewModelTest {
    private val viewModel: HomeScreenContract.ViewModel = HomeScreenViewModel()

    @Test
    fun `onDateFilterChange should assign value correctly`() {
        viewModel.onIntent(HomeScreenContract.Intent.OnDateFilterChange(VALID_DATE))

        assertEquals(VALID_DATE, viewModel.uiState.value.currentDateFilter)

    }

    private companion object {
        val VALID_DATE: LocalDate = LocalDate.of(1998, 10, 20)
    }
}