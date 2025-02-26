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

import androidx.compose.runtime.mutableStateOf
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.balancecard.BankAccountBalanceUiState
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.balancecard.BalanceCardViewModel
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.balancecard.BalanceCardViewModel.Companion.ACCOUNT_NAME_MOCK
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.balancecard.BalanceCardViewModel.Companion.ACCOUNT_VALUE_MOCK
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.balancecard.BalanceCardViewModel.Companion.BANK_NAME_MOCK
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.balancecard.BalanceCardViewModel.Companion.TOTAL_BALANCE_MOCK
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.creditcardbillscard.CreditCardBillUiState
import br.com.rbrthmn.ui.financialcompanion.utils.formatDouble
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate

class BalanceCardViewModelTest {
    private val viewModel = BalanceCardViewModel()

    @Test
    fun `doOnInit should assign initial values`() {
        viewModel.doOnInit()
        val expectedList = listOf(
            CreditCardBillUiState(
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
        viewModel.onInitialBalanceChange(EMPTY_STRING)

        assertEquals(false, viewModel.isNewAccountBalanceValid)
        assertEquals(EMPTY_STRING, viewModel.newAccountBalance)
    }

    @Test
    fun `onInitialBalanceChange with value should assign correctly`() {
        viewModel.onInitialBalanceChange(VALID_BALANCE_STRING)

        assertEquals(true, viewModel.isNewAccountBalanceValid)
        assertEquals(VALID_BALANCE_STRING, viewModel.newAccountBalance)
    }

    @Test
    fun `onDescriptionChange with empty value should assign correctly`() {
        viewModel.onDescriptionChange(EMPTY_STRING)

        assertEquals(false, viewModel.isNewAccountDescriptionValid)
        assertEquals(EMPTY_STRING, viewModel.newAccountDescription)
    }

    @Test
    fun `onDescriptionChange with value should assign correctly`() {
        viewModel.onDescriptionChange(VALID_STRING)

        assertEquals(true, viewModel.isNewAccountDescriptionValid)
        assertEquals(VALID_STRING, viewModel.newAccountDescription)
    }

    @Test
    fun `onBankChange with empty value should assign correctly`() {
        viewModel.onBankChange(VALID_ID_STRING, EMPTY_STRING)

        assertEquals(false, viewModel.isNewAccountBankValid)
        assertEquals(EMPTY_STRING, viewModel.newAccountBank)
        assertEquals(VALID_ID_STRING, viewModel.newAccountBankIcon)
    }

    @Test
    fun `onBankChange with value should assign correctly`() {
        viewModel.onBankChange(VALID_ID_STRING, VALID_BALANCE_STRING)

        assertEquals(true, viewModel.isNewAccountBankValid)
        assertEquals(VALID_BALANCE_STRING, viewModel.newAccountBank)
        assertEquals(VALID_ID_STRING, viewModel.newAccountBankIcon)
    }

    @Test
    fun `cleanNewAccount should assign default values`() {
        viewModel.cleanNewAccount()

        assertCleanedInputs()
    }

    @Test
    fun `onSaveClick with valid input should assign false to dialog`() {
        assignValidInputs()
        val mock = mutableStateOf(true)

        viewModel.onSaveClick(mock)

        assertEquals(false, mock.value)
    }

    @Test
    fun `onSaveClick with valid input should add new account`() {
        assignValidInputs()
        val mock = mutableStateOf(true)
        val newAccount = BankAccountBalanceUiState(
            name = VALID_STRING,
            value = FORMATTED_BALANCE_STRING,
            bankName = VALID_STRING
        )
        val newAccountList = viewModel.uiState.value.accounts + newAccount

        viewModel.onSaveClick(mock)

        assertEquals(newAccountList, viewModel.uiState.value.accounts)
    }

    @Test
    fun `onSaveClick with valid input should clean inputs`() {
        assignValidInputs()
        val mock = mutableStateOf(true)

        viewModel.onSaveClick(mock)

        assertCleanedInputs()
    }

    private fun assertCleanedInputs() {
        assertEquals(EMPTY_STRING, viewModel.newAccountBalance)
        assertEquals(EMPTY_STRING, viewModel.newAccountDescription)
        assertEquals(EMPTY_STRING, viewModel.newAccountBank)
        assertEquals(R.drawable.bank_icon, viewModel.newAccountBankIcon)
        assertEquals(true, viewModel.isNewAccountBankValid)
        assertEquals(true, viewModel.isNewAccountDescriptionValid)
        assertEquals(true, viewModel.isNewAccountBalanceValid)
    }

    private fun assignValidInputs() {
        viewModel.onInitialBalanceChange(VALID_BALANCE_STRING)
        viewModel.onDescriptionChange(VALID_STRING)
        viewModel.onBankChange(VALID_ID_STRING, VALID_STRING)
    }

    @Test
    fun `setDateFilter should assign value correctly`() {
        viewModel.setDateFilter(VALID_DATE)

        assertEquals(VALID_DATE, viewModel.currentDateFilter)
    }

    private companion object {
        const val VALID_BALANCE_STRING = "123"
        const val FORMATTED_BALANCE_STRING = "123.00"
        const val EMPTY_STRING = ""
        const val VALID_STRING = "test"
        const val VALID_ID_STRING = 1
        val VALID_DATE: LocalDate = LocalDate.of(1998, 10, 20)
    }
}

