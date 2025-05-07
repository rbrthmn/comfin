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
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.creditcardbillscard.CreditCardBillsCardContract.Intent
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.creditcardbillscard.CreditCardBillsCardViewModel
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.creditcardbillscard.CreditCardBillsCardViewModel.Companion.BANK_NAME_MOCK
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.creditcardbillscard.CreditCardBillsCardViewModel.Companion.BILL_VALUE_MOCK
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.creditcardbillscard.CreditCardBillsCardViewModel.Companion.CREDIT_CARD_MOCK
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.creditcardbillscard.CreditCardBillsCardViewModel.Companion.DUE_DAY_MOCK
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.creditcardbillscard.CreditCardBillsCardViewModel.Companion.TOTAL_BILL_MOCK
import br.com.rbrthmn.ui.financialcompanion.utils.formatDouble
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.creditcardbillscard.CreditCardBillsCardContract as Contract

class CreditCardBillsCardViewModelTest {
    private val viewModel = CreditCardBillsCardViewModel()

    @Test
    fun `doOnInit should assign initial values`() {
        viewModel.doOnInit()
        val expectedList = listOf(
            Contract.CreditCardBillUiState(
                name = CREDIT_CARD_MOCK,
                value = formatDouble(BILL_VALUE_MOCK),
                dueDay = DUE_DAY_MOCK,
                bankName = BANK_NAME_MOCK,
            )
        )

        assertEquals(formatDouble(TOTAL_BILL_MOCK), viewModel.uiState.value.totalBill)
        assertEquals(expectedList.first().value, viewModel.uiState.value.bills.first().value)
        assertEquals(expectedList.first().name, viewModel.uiState.value.bills.first().name)
        assertEquals(expectedList.first().bankName, viewModel.uiState.value.bills.first().bankName)
        assertEquals(expectedList.first().dueDay, viewModel.uiState.value.bills.first().dueDay)
    }

    @Test
    fun `OnNewCreditCardBillValueChange with empty value should assign correctly`() {
        viewModel.onIntent(Intent.OnNewCreditCardBillValueChange(EMPTY_STRING))

        assertEquals(false, viewModel.uiState.value.isNewCreditCardBillValid)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newCreditCardBill)
    }

    @Test
    fun `OnNewCreditCardBillValueChange with value should assign correctly`() {
        viewModel.onIntent(Intent.OnNewCreditCardBillValueChange(VALID_BILL_STRING))

        assertEquals(true, viewModel.uiState.value.isNewCreditCardBillValid)
        assertEquals(VALID_BILL_STRING, viewModel.uiState.value.newCreditCardBill)
    }

    @Test
    fun `onNewCreditCardNameChange with empty value should assign correctly`() {
        viewModel.onIntent(Intent.OnNewCreditCardNameChange(EMPTY_STRING))

        assertEquals(false, viewModel.uiState.value.isNewCreditCardNameValid)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newCreditCardName)
    }

    @Test
    fun `onNewCreditCardNameChange with value should assign correctly`() {
        viewModel.onIntent(Intent.OnNewCreditCardNameChange(VALID_STRING))

        assertEquals(true, viewModel.uiState.value.isNewCreditCardNameValid)
        assertEquals(VALID_STRING, viewModel.uiState.value.newCreditCardName)
    }

    @Test
    fun `onBankChange with empty value should assign correctly`() {
        viewModel.onIntent(Intent.OnBankChange(VALID_ID_STRING, EMPTY_STRING))

        assertEquals(false, viewModel.uiState.value.isNewCreditCardBankNameValid)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newCreditCardBankName)
        assertEquals(VALID_ID_STRING, viewModel.uiState.value.newCreditCardBankIcon)
    }

    @Test
    fun `onBankChange with value should assign correctly`() {
        viewModel.onIntent(Intent.OnBankChange(VALID_ID_STRING, VALID_BILL_STRING))

        assertEquals(true, viewModel.uiState.value.isNewCreditCardBankNameValid)
        assertEquals(VALID_BILL_STRING, viewModel.uiState.value.newCreditCardBankName)
        assertEquals(VALID_ID_STRING, viewModel.uiState.value.newCreditCardBankIcon)
    }

    @Test
    fun `onNewCreditCardBillDueDayChange with zero value should assign correctly`() {
        viewModel.onIntent(Intent.OnNewCreditCardBillDueDayChange(INVALID_DUE_DAY))

        assertEquals(false, viewModel.uiState.value.isNewCreditCardBillDueDayValid)
        assertEquals(INVALID_DUE_DAY, viewModel.uiState.value.newCreditCardBillDueDay)
    }

    @Test
    fun `onNewCreditCardBillDueDayChange with value should assign correctly`() {
        viewModel.onIntent(Intent.OnNewCreditCardBillDueDayChange(VALID_DUE_DAY))

        assertEquals(true, viewModel.uiState.value.isNewCreditCardBillDueDayValid)
        assertEquals(VALID_DUE_DAY, viewModel.uiState.value.newCreditCardBillDueDay)
    }

    @Test
    fun `cleanNewAccount should assign default values`() {
        viewModel.onIntent(Intent.CleanInputs)

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
        val newCard = Contract.CreditCardBillUiState(
            name = VALID_STRING,
            value = VALID_BILL_STRING,
            bankName = VALID_STRING,
        )
        val newCardsList = viewModel.uiState.value.bills + newCard

        viewModel.onIntent(Intent.OnSaveClick(mock))

        assertEquals(newCardsList, viewModel.uiState.value.bills)
    }

    @Test
    fun `onSaveClick with valid input should clean inputs`() {
        assignValidInputs()
        val mock = mutableStateOf(true)

        viewModel.onIntent(Intent.OnSaveClick(mock))

        assertCleanedInputs()
    }

    private fun assertCleanedInputs() {
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newCreditCardName)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newCreditCardBill)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newCreditCardBankName)
        assertEquals(R.drawable.bank_icon, viewModel.uiState.value.newCreditCardBankIcon)
        assertEquals(0, viewModel.uiState.value.newCreditCardBillDueDay)
        assertEquals(true, viewModel.uiState.value.isNewCreditCardBankNameValid)
        assertEquals(true, viewModel.uiState.value.isNewCreditCardNameValid)
        assertEquals(true, viewModel.uiState.value.isNewCreditCardBillValid)
        assertEquals(true, viewModel.uiState.value.isNewCreditCardBillDueDayValid)
    }

    private fun assignValidInputs() {
        viewModel.run {
            onIntent(Intent.OnNewCreditCardNameChange(VALID_STRING))
            onIntent(Intent.OnNewCreditCardBillValueChange(VALID_BILL_STRING))
            onIntent(Intent.OnBankChange(R.drawable.bank_icon, VALID_STRING))
            onIntent(Intent.OnNewCreditCardBillDueDayChange(VALID_DUE_DAY))
        }
    }

    @Test
    fun `setDateFilter should assign value correctly`() {
        viewModel.onIntent(Intent.OnDateFilterChange(VALID_DATE))

        assertEquals(VALID_DATE, viewModel.uiState.value.currentDateFilter)
    }

    private companion object {
        const val VALID_BILL_STRING = "123"
        const val EMPTY_STRING = ""
        const val VALID_STRING = "test"
        const val VALID_ID_STRING = 1
        const val VALID_DUE_DAY = 1
        const val INVALID_DUE_DAY = 0
        val VALID_DATE: LocalDate = LocalDate.of(1998, 10, 20)
    }
}