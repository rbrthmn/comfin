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

package viewmodels

import androidx.compose.runtime.mutableStateOf
import br.com.rbrthmn.ui.financialcompanion.uistates.FinancialOverviewUiState
import br.com.rbrthmn.ui.financialcompanion.viewmodels.BalanceCardViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Test

class BalanceCardViewModelTest {
    private val viewModel = BalanceCardViewModel()

    @Test
    fun onInitialBalanceChange_with_empty_value_should_assign_correctly() {
        viewModel.onInitialBalanceChange(EMPTY_STRING)

        assertEquals(false, viewModel.isNewAccountBalanceValid)
        assertEquals(EMPTY_STRING, viewModel.newAccountBalance)
    }

    @Test
    fun onInitialBalanceChange_with_value_should_assign_correctly() {
        viewModel.onInitialBalanceChange(VALID_BALANCE_STRING)

        assertEquals(true, viewModel.isNewAccountBalanceValid)
        assertEquals(VALID_BALANCE_STRING, viewModel.newAccountBalance)
    }

    @Test
    fun onDescriptionChange_with_empty_value_should_assign_correctly() {
        viewModel.onDescriptionChange(EMPTY_STRING)

        assertEquals(false, viewModel.isNewAccountDescriptionValid)
        assertEquals(EMPTY_STRING, viewModel.newAccountDescription)
    }

    @Test
    fun onDescriptionChange_with_value_should_assign_correctly() {
        viewModel.onDescriptionChange(VALID_STRING)

        assertEquals(true, viewModel.isNewAccountDescriptionValid)
        assertEquals(VALID_STRING, viewModel.newAccountDescription)
    }

    @Test
    fun onBankChange_with_empty_value_should_assign_correctly() {
        viewModel.onBankChange(VALID_ID_STRING, EMPTY_STRING)

        assertEquals(false, viewModel.isNewAccountBankValid)
        assertEquals(EMPTY_STRING, viewModel.newAccountBank)
        assertEquals(VALID_ID_STRING, viewModel.newAccountBankIcon)
    }

    @Test
    fun onBankChange_with_value_should_assign_correctly() {
        viewModel.onBankChange(VALID_ID_STRING, VALID_BALANCE_STRING)

        assertEquals(true, viewModel.isNewAccountBankValid)
        assertEquals(VALID_BALANCE_STRING, viewModel.newAccountBank)
        assertEquals(VALID_ID_STRING, viewModel.newAccountBankIcon)
    }

    @Test
    fun cleanNewAccount_should_assign_default_values() {
        viewModel.cleanNewAccount()

        assertEquals(EMPTY_STRING, viewModel.newAccountBalance)
        assertEquals(EMPTY_STRING, viewModel.newAccountDescription)
        assertEquals(EMPTY_STRING, viewModel.newAccountBank)
        assertEquals(-1, viewModel.newAccountBankIcon)
        assertEquals(true, viewModel.isNewAccountBankValid)
        assertEquals(true, viewModel.isNewAccountDescriptionValid)
        assertEquals(true, viewModel.isNewAccountBalanceValid)
    }

    @Test
    fun onSaveClick_with_valid_input_should_assign_false_to_dialog() {
        assignValidInputs()
        val mock = mutableStateOf(true)

        viewModel.onSaveClick(mock)

        assertEquals(false, mock.value)
    }

    @Test
    fun onSaveClick_with_valid_input_should_add_new_account() {
        assignValidInputs()
        val mock = mutableStateOf(true)
        val newAccount = FinancialOverviewUiState(
            name = VALID_STRING,
            value = FORMATTED_BALANCE_STRING,
            financialInstitutionName = VALID_STRING
        )
        val newAccountList = viewModel.uiState.value.accounts + newAccount

        viewModel.onSaveClick(mock)

        assertEquals(newAccountList, viewModel.uiState.value.accounts)
    }

    @Test
    fun onSaveClick_with_valid_input_should_clean_inputs() {
        assignValidInputs()
        val mock = mutableStateOf(true)

        viewModel.onSaveClick(mock)

        assertEquals(EMPTY_STRING, viewModel.newAccountBalance)
        assertEquals(EMPTY_STRING, viewModel.newAccountDescription)
        assertEquals(EMPTY_STRING, viewModel.newAccountBank)
        assertEquals(-1, viewModel.newAccountBankIcon)
        assertEquals(true, viewModel.isNewAccountBankValid)
        assertEquals(true, viewModel.isNewAccountDescriptionValid)
        assertEquals(true, viewModel.isNewAccountBalanceValid)
    }

    private fun assignValidInputs() {
        viewModel.onInitialBalanceChange(VALID_BALANCE_STRING)
        viewModel.onDescriptionChange(VALID_STRING)
        viewModel.onBankChange(VALID_ID_STRING, VALID_STRING)
    }

    private companion object {
        const val VALID_BALANCE_STRING = "123"
        const val FORMATTED_BALANCE_STRING = "123.00"
        const val EMPTY_STRING = ""
        const val VALID_STRING = "test"
        const val VALID_ID_STRING = 1
    }
}

