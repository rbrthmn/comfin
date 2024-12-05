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
import br.com.rbrthmn.ui.financialcompanion.uistates.CreditCardBillUiState
import br.com.rbrthmn.ui.financialcompanion.viewmodels.CreditCardBillsCardViewModel
import junit.framework.TestCase
import org.junit.Test

class CreditCardBillsCardViewModelTest {
    private val viewModel = CreditCardBillsCardViewModel()
    
    @Test
    fun onNewCreditCardBillChange_with_empty_value_should_assign_correctly() {
        viewModel.onNewCreditCardBillChange(EMPTY_STRING)

        TestCase.assertEquals(false, viewModel.isNewCreditCardBillValid)
        TestCase.assertEquals(EMPTY_STRING, viewModel.newCreditCardBill)
    }

    @Test
    fun onNewCreditCardBillChange_with_value_should_assign_correctly() {
        viewModel.onNewCreditCardBillChange(VALID_BILL_STRING)

        TestCase.assertEquals(true, viewModel.isNewCreditCardBillValid)
        TestCase.assertEquals(VALID_BILL_STRING, viewModel.newCreditCardBill)
    }

    @Test
    fun onNewCreditCardNameChange_with_empty_value_should_assign_correctly() {
        viewModel.onNewCreditCardNameChange(EMPTY_STRING)

        TestCase.assertEquals(false, viewModel.isNewCreditCardNameValid)
        TestCase.assertEquals(EMPTY_STRING, viewModel.newCreditCardName)
    }

    @Test
    fun onNewCreditCardNameChange_with_value_should_assign_correctly() {
        viewModel.onNewCreditCardNameChange(VALID_STRING)

        TestCase.assertEquals(true, viewModel.isNewCreditCardNameValid)
        TestCase.assertEquals(VALID_STRING, viewModel.newCreditCardName)
    }

    @Test
    fun onBankChange_with_empty_value_should_assign_correctly() {
        viewModel.onBankChange(VALID_ID_STRING, EMPTY_STRING)

        TestCase.assertEquals(false, viewModel.isNewCreditCardBankNameValid)
        TestCase.assertEquals(EMPTY_STRING, viewModel.newCreditCardBankName)
        TestCase.assertEquals(VALID_ID_STRING, viewModel.newCreditCardBankIcon)
    }

    @Test
    fun onBankChange_with_value_should_assign_correctly() {
        viewModel.onBankChange(VALID_ID_STRING, VALID_BILL_STRING)

        TestCase.assertEquals(true, viewModel.isNewCreditCardBankNameValid)
        TestCase.assertEquals(VALID_BILL_STRING, viewModel.newCreditCardBankName)
        TestCase.assertEquals(VALID_ID_STRING, viewModel.newCreditCardBankIcon)
    }

    @Test
    fun onNewCreditCardBillDueDayChange_with_zero_value_should_assign_correctly() {
        viewModel.onNewCreditCardBillDueDayChange(INVALID_DUE_DAY)

        TestCase.assertEquals(false, viewModel.isNewCreditCardBillDueDayValid)
        TestCase.assertEquals(INVALID_DUE_DAY, viewModel.newCreditCardBillDueDay)
    }

    @Test
    fun onNewCreditCardBillDueDayChange_with_value_should_assign_correctly() {
        viewModel.onNewCreditCardBillDueDayChange(VALID_DUE_DAY)

        TestCase.assertEquals(true, viewModel.isNewCreditCardBillDueDayValid)
        TestCase.assertEquals(VALID_DUE_DAY, viewModel.newCreditCardBillDueDay)
    }

    @Test
    fun cleanNewAccount_should_assign_default_values() {
        viewModel.cleanInputs()

        assertCleanedInputs()
    }

    @Test
    fun onSaveClick_with_valid_input_should_assign_false_to_dialog() {
        assignValidInputs()
        val mock = mutableStateOf(true)

        viewModel.onSaveClick(mock)

        TestCase.assertEquals(false, mock.value)
    }

    @Test
    fun onSaveClick_with_valid_input_should_add_new_account() {
        assignValidInputs()
        val mock = mutableStateOf(true)
        val newCard = CreditCardBillUiState(
            name = VALID_STRING,
            value = FORMATTED_BILL_STRING,
            bankName = VALID_STRING,
        )
        val newCardsList = viewModel.uiState.value.bills + newCard

        viewModel.onSaveClick(mock)

        TestCase.assertEquals(newCardsList, viewModel.uiState.value.bills)
    }

    @Test
    fun onSaveClick_with_valid_input_should_clean_inputs() {
        assignValidInputs()
        val mock = mutableStateOf(true)

        viewModel.onSaveClick(mock)

        assertCleanedInputs()
    }

    private fun assertCleanedInputs() {
        TestCase.assertEquals(EMPTY_STRING, viewModel.newCreditCardName)
        TestCase.assertEquals(EMPTY_STRING, viewModel.newCreditCardBill)
        TestCase.assertEquals(EMPTY_STRING, viewModel.newCreditCardBankName)
        TestCase.assertEquals(R.drawable.bank_icon, viewModel.newCreditCardBankIcon)
        TestCase.assertEquals(0, viewModel.newCreditCardBillDueDay)
        TestCase.assertEquals(true, viewModel.isNewCreditCardBankNameValid)
        TestCase.assertEquals(true, viewModel.isNewCreditCardNameValid)
        TestCase.assertEquals(true, viewModel.isNewCreditCardBillValid)
        TestCase.assertEquals(true, viewModel.isNewCreditCardBillDueDayValid)
    }

    private fun assignValidInputs() {
        viewModel.onNewCreditCardBillChange(FORMATTED_BILL_STRING)
        viewModel.onNewCreditCardNameChange(VALID_STRING)
        viewModel.onBankChange(R.drawable.bank_icon, VALID_STRING)
        viewModel.onNewCreditCardBillDueDayChange(VALID_DUE_DAY)
    }

    private companion object {
        const val VALID_BILL_STRING = "123"
        const val FORMATTED_BILL_STRING = "123.00"
        const val EMPTY_STRING = ""
        const val VALID_STRING = "test"
        const val VALID_ID_STRING = 1
        const val VALID_DUE_DAY = 1
        const val INVALID_DUE_DAY = 0
    }
}