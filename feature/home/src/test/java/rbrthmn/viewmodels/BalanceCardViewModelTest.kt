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

package rbrthmn.viewmodels

import androidx.compose.runtime.mutableStateOf
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardContract
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardContract.Intent
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardViewModel
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardViewModel.Companion.ACCOUNT_NAME_MOCK
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardViewModel.Companion.ACCOUNT_VALUE_MOCK
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardViewModel.Companion.BANK_NAME_MOCK
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardViewModel.Companion.TOTAL_BALANCE_MOCK
import br.com.rbrthmn.ui.R
import br.com.rbrthmn.ui.utils.formatDouble
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate

class BalanceCardViewModelTest {
    private val viewModel = BalanceCardViewModel()

    @Test
    fun `doOnInit should assign initial values`() {
        viewModel.doOnInit()
        val expectedList = listOf(
            br.com.rbrthmn.home.ui.components.balancecard.BalanceCardContract.BankAccountBalanceUiState(
                name = ACCOUNT_NAME_MOCK,
                value = formatDouble(ACCOUNT_VALUE_MOCK),
                bankName = BANK_NAME_MOCK,
            )
        )

        assertEquals(formatDouble(TOTAL_BALANCE_MOCK), viewModel.uiState.value.totalBalance)
        assertEquals(expectedList.first().value, viewModel.uiState.value.accounts.first().value)
        assertEquals(expectedList.first().name, viewModel.uiState.value.accounts.first().name)
        assertEquals(
            expectedList.first().bankName,
            viewModel.uiState.value.accounts.first().bankName
        )
    }

    @Test
    fun `onInitialBalanceChange with empty value should assign correctly`() {
        viewModel.onIntent(Intent.OnInitialBalanceChange(EMPTY_STRING))

        assertEquals(false, viewModel.uiState.value.isNewAccountBalanceValid)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newAccountBalance)
    }

    @Test
    fun `onInitialBalanceChange with value should assign correctly`() {
        viewModel.onIntent(Intent.OnInitialBalanceChange(VALID_BALANCE_STRING))

        assertEquals(true, viewModel.uiState.value.isNewAccountBalanceValid)
        assertEquals(VALID_BALANCE_STRING, viewModel.uiState.value.newAccountBalance)
    }

    @Test
    fun `onDescriptionChange with empty value should assign correctly`() {
        viewModel.onIntent(Intent.OnDescriptionChange(EMPTY_STRING))

        assertEquals(false, viewModel.uiState.value.isNewAccountDescriptionValid)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newAccountDescription)
    }

    @Test
    fun `onDescriptionChange with value should assign correctly`() {
        viewModel.onIntent(Intent.OnDescriptionChange(VALID_STRING))

        assertEquals(true, viewModel.uiState.value.isNewAccountDescriptionValid)
        assertEquals(VALID_STRING, viewModel.uiState.value.newAccountDescription)
    }

    @Test
    fun `onBankChange with empty value should assign correctly`() {
        viewModel.onIntent(Intent.OnBankChange(VALID_ID_STRING, EMPTY_STRING))

        assertEquals(false, viewModel.uiState.value.isNewAccountBankValid)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newAccountBank)
        assertEquals(VALID_ID_STRING, viewModel.uiState.value.newAccountBankIcon)
    }

    @Test
    fun `onBankChange with value should assign correctly`() {
        viewModel.onIntent(Intent.OnBankChange(VALID_ID_STRING, VALID_BALANCE_STRING))

        assertEquals(true, viewModel.uiState.value.isNewAccountBankValid)
        assertEquals(VALID_BALANCE_STRING, viewModel.uiState.value.newAccountBank)
        assertEquals(VALID_ID_STRING, viewModel.uiState.value.newAccountBankIcon)
    }

    @Test
    fun `cleanNewAccount should assign default values`() {
        viewModel.onIntent(Intent.CleanNewAccount)

        assertCleanedInputs()
    }

    @Test
    fun `onSaveClick with valid input should assign false to dialog`() {
        assignValidInputs()
        val mock = mutableStateOf(true)

        viewModel.onIntent(Intent.OnSaveClick(mock))

        assertEquals(false, mock.value)
    }

    @Test
    fun `onSaveClick with valid input should add new account`() {
        assignValidInputs()
        val mock = mutableStateOf(true)
        val newAccount =
            BalanceCardContract.BankAccountBalanceUiState(
                name = VALID_STRING,
                value = FORMATTED_BALANCE_STRING,
                bankName = VALID_STRING
            )
        val newAccountList = viewModel.uiState.value.accounts + newAccount

        viewModel.onIntent(Intent.OnSaveClick(mock))

        assertEquals(newAccountList, viewModel.uiState.value.accounts)
    }

    @Test
    fun `onSaveClick with valid input should clean inputs`() {
        assignValidInputs()
        val mock = mutableStateOf(true)

        viewModel.onIntent(Intent.OnSaveClick(mock))

        assertCleanedInputs()
    }

    private fun assertCleanedInputs() {
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newAccountBalance)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newAccountDescription)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newAccountBank)
        assertEquals(R.drawable.bank_icon, viewModel.uiState.value.newAccountBankIcon)
        assertEquals(true, viewModel.uiState.value.isNewAccountBankValid)
        assertEquals(true, viewModel.uiState.value.isNewAccountDescriptionValid)
        assertEquals(true, viewModel.uiState.value.isNewAccountBalanceValid)
    }

    private fun assignValidInputs() {
        viewModel.run {
            onIntent(Intent.OnInitialBalanceChange(VALID_BALANCE_STRING))
            onIntent(Intent.OnDescriptionChange(VALID_STRING))
            onIntent(Intent.OnBankChange(VALID_ID_STRING, VALID_STRING))
        }
    }

    @Test
    fun `setDateFilter should assign value correctly`() {
        viewModel.onIntent(Intent.OnDateFilterChange(VALID_DATE))

        assertEquals(VALID_DATE, viewModel.uiState.value.currentDateFilter)
    }

    private companion object {
        const val VALID_BALANCE_STRING = "123"
        const val FORMATTED_BALANCE_STRING = "123,00"
        const val EMPTY_STRING = ""
        const val VALID_STRING = "test"
        const val VALID_ID_STRING = 1
        val VALID_DATE: LocalDate = LocalDate.of(1998, 10, 20)
    }
}

