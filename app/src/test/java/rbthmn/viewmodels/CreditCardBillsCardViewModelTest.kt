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
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.creditcardbillscard.CreditCardBillUiState
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.creditcardbillscard.CreditCardBillsCardViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate

class CreditCardBillsCardViewModelTest {
    private val viewModel = CreditCardBillsCardViewModel()
    
    @Test
    fun `onNewCreditCardBillChange with empty value should assign correctly`() { 
        viewModel.onNewCreditCardBillChange(EMPTY_STRING)

        assertEquals(false, viewModel.uiState.value.isNewCreditCardBillValid)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newCreditCardBill)
    }

    @Test
    fun `onNewCreditCardBillChange with value should assign correctly`() { 
        viewModel.onNewCreditCardBillChange(VALID_BILL_STRING)

        assertEquals(true, viewModel.uiState.value.isNewCreditCardBillValid)
        assertEquals(VALID_BILL_STRING, viewModel.uiState.value.newCreditCardBill)
    }

    @Test
    fun `onNewCreditCardNameChange with empty value should assign correctly`() { 
        viewModel.onNewCreditCardNameChange(EMPTY_STRING)

        assertEquals(false, viewModel.uiState.value.isNewCreditCardNameValid)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newCreditCardName)
    }

    @Test
    fun `onNewCreditCardNameChange with value should assign correctly`() { 
        viewModel.onNewCreditCardNameChange(VALID_STRING)

        assertEquals(true, viewModel.uiState.value.isNewCreditCardNameValid)
        assertEquals(VALID_STRING, viewModel.uiState.value.newCreditCardName)
    }

    @Test
    fun `onBankChange with empty value should assign correctly`() { 
        viewModel.onBankChange(VALID_ID_STRING, EMPTY_STRING)

        assertEquals(false, viewModel.uiState.value.isNewCreditCardBankNameValid)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newCreditCardBankName)
        assertEquals(VALID_ID_STRING, viewModel.uiState.value.newCreditCardBankIcon)
    }

    @Test
    fun `onBankChange with value should assign correctly`() { 
        viewModel.onBankChange(VALID_ID_STRING, VALID_BILL_STRING)

        assertEquals(true, viewModel.uiState.value.isNewCreditCardBankNameValid)
        assertEquals(VALID_BILL_STRING, viewModel.uiState.value.newCreditCardBankName)
        assertEquals(VALID_ID_STRING, viewModel.uiState.value.newCreditCardBankIcon)
    }

    @Test
    fun `onNewCreditCardBillDueDayChange with zero value should assign correctly`() { 
        viewModel.onNewCreditCardBillDueDayChange(INVALID_DUE_DAY)

        assertEquals(false, viewModel.uiState.value.isNewCreditCardBillDueDayValid)
        assertEquals(INVALID_DUE_DAY, viewModel.uiState.value.newCreditCardBillDueDay)
    }

    @Test
    fun `onNewCreditCardBillDueDayChange with value should assign correctly`() { 
        viewModel.onNewCreditCardBillDueDayChange(VALID_DUE_DAY)

        assertEquals(true, viewModel.uiState.value.isNewCreditCardBillDueDayValid)
        assertEquals(VALID_DUE_DAY, viewModel.uiState.value.newCreditCardBillDueDay)
    }

    @Test
    fun `cleanNewAccount should assign default values`() {
        viewModel.cleanInputs()

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
        val newCard = CreditCardBillUiState(
            name = VALID_STRING,
            value = FORMATTED_BILL_STRING,
            bankName = VALID_STRING,
        )
        val newCardsList = viewModel.uiState.value.bills + newCard

        viewModel.onSaveClick(mock)

        assertEquals(newCardsList, viewModel.uiState.value.bills)
    }

    @Test
    fun `onSaveClick with valid input should clean inputs`() {
        assignValidInputs()
        val mock = mutableStateOf(true)

        viewModel.onSaveClick(mock)

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
        viewModel.onNewCreditCardBillChange(FORMATTED_BILL_STRING)
        viewModel.onNewCreditCardNameChange(VALID_STRING)
        viewModel.onBankChange(R.drawable.bank_icon, VALID_STRING)
        viewModel.onNewCreditCardBillDueDayChange(VALID_DUE_DAY)
    }

    @Test
    fun `setDateFilter should assign value correctly`() {
        viewModel.setDateFilter(VALID_DATE)

        assertEquals(VALID_DATE, viewModel.uiState.value.currentDateFilter)
    }

    private companion object {
        const val VALID_BILL_STRING = "123"
        const val FORMATTED_BILL_STRING = "123.00"
        const val EMPTY_STRING = ""
        const val VALID_STRING = "test"
        const val VALID_ID_STRING = 1
        const val VALID_DUE_DAY = 1
        const val INVALID_DUE_DAY = 0
        val VALID_DATE: LocalDate = LocalDate.of(1998, 10, 20)
    }
}