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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import br.com.rbrthmn.model.OperationType
import br.com.rbrthmn.ui.financialcompanion.screens.operations.OperationsScreenContract.Intent
import br.com.rbrthmn.ui.financialcompanion.screens.operations.OperationsScreenViewModel
import br.com.rbrthmn.ui.financialcompanion.screens.operations.OperationsScreenViewModel.Companion.TOTAL_BALANCE_MOCK
import br.com.rbrthmn.ui.financialcompanion.screens.operations.OperationsScreenViewModel.Companion.TOTAL_INCOME_MOCK
import br.com.rbrthmn.ui.financialcompanion.screens.operations.OperationsScreenViewModel.Companion.TOTAL_OUTCOME_MOCK
import br.com.rbrthmn.ui.financialcompanion.utils.StringProvider
import br.com.rbrthmn.ui.financialcompanion.utils.formatDouble
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class OperationsScreenViewModelTest {
    private lateinit var viewModel: OperationsScreenViewModel
    private val stringProvider: StringProvider = mockk(relaxed = true)

    @Before
    fun setup() {
        viewModel = OperationsScreenViewModel(stringProvider)
    }

    @Test
    fun `doOnInit should assign initial values`() {
        viewModel.doOnInit()

        assertEquals(formatDouble(TOTAL_BALANCE_MOCK), viewModel.uiState.value.totalBalance)
        assertEquals(formatDouble(TOTAL_INCOME_MOCK), viewModel.uiState.value.totalIncome)
        assertEquals(formatDouble(TOTAL_OUTCOME_MOCK), viewModel.uiState.value.totalOutcome)
        assertEquals(emptyList<@Composable () -> Unit>(), viewModel.uiState.value.dialogFields)
        assertTrue(viewModel.uiState.value.operations.isNotEmpty())
    }

    @Test
    fun `onDescriptionChange with empty value should be invalid`() {
        viewModel.onIntent(Intent.OnDescriptionChange(EMPTY_STRING))

        assertFalse(viewModel.uiState.value.isNewOperationDescriptionValid)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newOperationDescription)
    }

    @Test
    fun `onDescriptionChange with valid value should be valid`() {
        viewModel.onIntent(Intent.OnDescriptionChange(VALID_DESCRIPTION))

        assertTrue(viewModel.uiState.value.isNewOperationDescriptionValid)
        assertEquals(VALID_DESCRIPTION, viewModel.uiState.value.newOperationDescription)
    }

    @Test
    fun `onValueChange with invalid format should be invalid`() {
        viewModel.onIntent(Intent.OnValueChange(INVALID_VALUE))

        assertFalse(viewModel.uiState.value.isNewOperationValueValid)
        assertEquals(INVALID_VALUE, viewModel.uiState.value.newOperationValue)
    }

    @Test
    fun `onValueChange with valid format should be valid`() {
        viewModel.onIntent(Intent.OnValueChange(VALID_VALUE))

        assertTrue(viewModel.uiState.value.isNewOperationValueValid)
        assertEquals(VALID_VALUE, viewModel.uiState.value.newOperationValue)
    }

    @Test
    fun `onOperationTypeChange should update type and be valid`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(VALID_OPERATION_TYPE))

        assertTrue(viewModel.uiState.value.isNewOperationTypeValid)
        assertEquals(VALID_OPERATION_TYPE, viewModel.uiState.value.newOperationType)
    }

    @Test
    fun `onOperationTypeChange with TRANSFER should have 2 new operation fields`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.TRANSFER))
        val fields = viewModel.uiState.value.dialogFields

        assertEquals(2, fields.size)
    }

    @Test
    fun `onOperationTypeChange with DEBIT_PURCHASE should have 1 new operation field`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.DEBIT_PURCHASE))
        val fields = viewModel.uiState.value.dialogFields

        assertEquals(1, fields.size)
    }

    @Test
    fun `onOperationTypeChange with BILL_PAYMENT should have 1 new operation field`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.BILL_PAYMENT))
        val fields = viewModel.uiState.value.dialogFields

        assertEquals(1, fields.size)
    }

    @Test
    fun `onOperationTypeChange with WITHDRAWAL should have 1 new operation field`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.WITHDRAWAL))
        val fields = viewModel.uiState.value.dialogFields

        assertEquals(1, fields.size)
    }

    @Test
    fun `onOperationTypeChange with DEPOSIT should have 1 new operation field`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.DEPOSIT))
        val fields = viewModel.uiState.value.dialogFields

        assertEquals(1, fields.size)
    }

    @Test
    fun `onOperationTypeChange with INCOME should have 1 new operation field`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.INCOME))
        val fields = viewModel.uiState.value.dialogFields

        assertEquals(1, fields.size)
    }

    @Test
    fun `onOperationTypeChange with RESERVE_ALLOCATION should have 2 new operation fields`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.RESERVE_ALLOCATION))
        val fields = viewModel.uiState.value.dialogFields

        assertEquals(2, fields.size)
    }

    @Test
    fun `onOperationTypeChange with RESERVE_WITHDRAWAL should have 2 new operation fields`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.RESERVE_WITHDRAWAL))
        val fields = viewModel.uiState.value.dialogFields

        assertEquals(2, fields.size)
    }

    @Test
    fun `onOperationTypeChange with OTHER should have 1 new operation field`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.OTHER))
        val fields = viewModel.uiState.value.dialogFields

        assertEquals(1, fields.size)
    }

    @Test
    fun `onOriginAccountChange with valid account should update account if required`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.DEBIT_PURCHASE))
        viewModel.onIntent(Intent.OnOriginAccountChange(VALID_ACCOUNT))

        assertTrue(viewModel.uiState.value.isNewOperationOriginAccountValid)
        assertEquals(VALID_ACCOUNT, viewModel.uiState.value.newOperationOriginAccount)
    }

    @Test
    fun `onOriginAccountChange with invalid account should update account if required`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.DEBIT_PURCHASE))
        viewModel.onIntent(Intent.OnOriginAccountChange(EMPTY_STRING))

        assertFalse(viewModel.uiState.value.isNewOperationOriginAccountValid)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newOperationOriginAccount)
    }

    @Test
    fun `onDestinationAccountChange with invalid account should update account if required`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.DEPOSIT))
        viewModel.onIntent(Intent.OnDestinationAccountChange(EMPTY_STRING))

        assertFalse(viewModel.uiState.value.isNewOperationDestinationAccountValid)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newOperationDestinationAccount)
    }

    @Test
    fun `onDestinationAccountChange with valid account should update account if required`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.DEPOSIT))
        viewModel.onIntent(Intent.OnDestinationAccountChange(VALID_DESTINATION_ACCOUNT))


        assertTrue(viewModel.uiState.value.isNewOperationDestinationAccountValid)
        assertEquals(
            VALID_DESTINATION_ACCOUNT,
            viewModel.uiState.value.newOperationDestinationAccount
        )
    }

    @Test
    fun `onReserveChange should update reserve if required`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.RESERVE_ALLOCATION))
        viewModel.onIntent(Intent.OnReserveChange(VALID_RESERVE))

        assertTrue(viewModel.uiState.value.isNewOperationReserveValid)
        assertEquals(VALID_RESERVE, viewModel.uiState.value.newOperationReserve)
    }

    @Test
    fun `onSaveButtonClick with valid inputs should reset fields`() {
        viewModel.run {
            onIntent(Intent.OnOperationTypeChange(VALID_OPERATION_TYPE))
            onIntent(Intent.OnDescriptionChange(VALID_TRANSACTION))
            onIntent(Intent.OnValueChange(VALID_VALUE))
            onIntent(Intent.OnDestinationAccountChange(VALID_DESTINATION_ACCOUNT))
        }
        val mockDialog = mockk<MutableState<Boolean>>(relaxed = true)
        val expectedNewOperationsSize = viewModel.uiState.value.operations.size + 1

        viewModel.onIntent(Intent.OnSaveButtonClick(mockDialog))

        verify { mockDialog.value = false }
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newOperationDescription)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newOperationValue)
        assertEquals(expectedNewOperationsSize, viewModel.uiState.value.operations.size)
        assertNull(viewModel.uiState.value.newOperationType)
    }

    @Test
    fun `onSearchQueryChange with operation type query should filter operations`() = runBlocking {
        viewModel.onIntent(Intent.OnSearchQueryChange(SEARCH_QUERY))
        val filteredOperations = viewModel.uiState.first().operations

        assertTrue(filteredOperations.all {
            it.type.contains(
                SEARCH_QUERY,
                ignoreCase = true
            )
        })
    }

    @Test
    fun `onSearchQueryChange with operation description query should filter operations`() =
        runBlocking {
            viewModel.onIntent(Intent.OnSearchQueryChange(VALID_DESCRIPTION))
            val filteredOperations = viewModel.uiState.first().operations

            assertTrue(filteredOperations.all {
                it.description.contains(
                    VALID_DESCRIPTION,
                    ignoreCase = true
                )
            })
        }

    @Test
    fun `onSearchQueryChange with operation value query should filter operations`() = runBlocking {
        viewModel.onIntent(Intent.OnSearchQueryChange(VALID_VALUE))
        val filteredOperations = viewModel.uiState.first().operations

        assertTrue(filteredOperations.all {
            it.value.contains(
                VALID_VALUE,
                ignoreCase = true
            )
        })
    }

    @Test
    fun `onSearchQueryChange with operation extras query should filter operations`() = runBlocking {
        viewModel.onIntent(Intent.OnSearchQueryChange(VALID_DESTINATION_ACCOUNT))
        val filteredOperations = viewModel.uiState.first().operations

        assertTrue(filteredOperations.all {
            it.extras?.contains(
                VALID_DESTINATION_ACCOUNT,
                ignoreCase = true
            ) == true
        })
    }

    @Test
    fun `validateFields with empty description should be invalid`() {
        viewModel.onIntent(Intent.OnDescriptionChange(EMPTY_STRING))

        assertFalse(viewModel.validateFields())
    }

    @Test
    fun `validateFields with invalid value format should be invalid`() {
        viewModel.onIntent(Intent.OnDescriptionChange(VALID_DESCRIPTION))
        viewModel.onIntent(Intent.OnValueChange(INVALID_VALUE))

        assertFalse(viewModel.validateFields())
    }

    @Test
    fun `validateFields with valid data should be valid`() {
        viewModel.run {
            onIntent(Intent.OnOperationTypeChange(VALID_OPERATION_TYPE))
            onIntent(Intent.OnDestinationAccountChange(VALID_DESTINATION_ACCOUNT))
            onIntent(Intent.OnDescriptionChange(VALID_DESCRIPTION))
            onIntent(Intent.OnValueChange(VALID_VALUE))
        }

        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `validateFields for TRANSFER should require both origin and destination accounts`() {
        viewModel.run {
            onIntent(Intent.OnOperationTypeChange(OperationType.TRANSFER))
            onIntent(Intent.OnDescriptionChange(VALID_DESCRIPTION))
            onIntent(Intent.OnValueChange(VALID_VALUE))
            onIntent(Intent.OnOriginAccountChange(VALID_ACCOUNT))
            onIntent(Intent.OnDestinationAccountChange(VALID_DESTINATION_ACCOUNT))
        }

        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `validateFields for DEBIT_PURCHASE should require origin account`() {
        viewModel.run {
            onIntent(Intent.OnOperationTypeChange(OperationType.DEBIT_PURCHASE))
            onIntent(Intent.OnDescriptionChange(VALID_DESCRIPTION))
            onIntent(Intent.OnValueChange(VALID_VALUE))
            onIntent(Intent.OnOriginAccountChange(VALID_ACCOUNT))
        }

        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `validateFields for BILL_PAYMENT should require origin account`() {
        viewModel.run {
            onIntent(Intent.OnOperationTypeChange(OperationType.BILL_PAYMENT))
            onIntent(Intent.OnDescriptionChange(VALID_DESCRIPTION))
            onIntent(Intent.OnValueChange(VALID_VALUE))
            onIntent(Intent.OnOriginAccountChange(VALID_ACCOUNT))
        }

        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `validateFields for WITHDRAWAL should require origin account`() {
        viewModel.run {
            onIntent(Intent.OnOperationTypeChange(OperationType.WITHDRAWAL))
            onIntent(Intent.OnDescriptionChange(VALID_DESCRIPTION))
            onIntent(Intent.OnValueChange(VALID_VALUE))
            onIntent(Intent.OnOriginAccountChange(VALID_ACCOUNT))
        }

        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `validateFields for DEPOSIT should require origin account`() {
        viewModel.run {
            onIntent(Intent.OnOperationTypeChange(OperationType.DEPOSIT))
            onIntent(Intent.OnDescriptionChange(VALID_DESCRIPTION))
            onIntent(Intent.OnValueChange(VALID_VALUE))
            onIntent(Intent.OnDestinationAccountChange(VALID_DESTINATION_ACCOUNT))
        }

        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `validateFields for INCOME should require origin account`() {
        viewModel.run {
            onIntent(Intent.OnOperationTypeChange(OperationType.INCOME))
            onIntent(Intent.OnDescriptionChange(VALID_DESCRIPTION))
            onIntent(Intent.OnValueChange(VALID_VALUE))
            onIntent(Intent.OnDestinationAccountChange(VALID_DESTINATION_ACCOUNT))
        }

        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `validateFields for RESERVE_ALLOCATION should require reserve and origin account`() {
        viewModel.run {
            onIntent(Intent.OnOperationTypeChange(OperationType.RESERVE_ALLOCATION))
            onIntent(Intent.OnDescriptionChange(VALID_DESCRIPTION))
            onIntent(Intent.OnValueChange(VALID_VALUE))
            onIntent(Intent.OnOriginAccountChange(VALID_ACCOUNT))
            onIntent(Intent.OnReserveChange(VALID_RESERVE))
        }

        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `validateFields for RESERVE_WITHDRAWAL should require reserve and destination account`() {
        viewModel.run {
            onIntent(Intent.OnOperationTypeChange(OperationType.RESERVE_WITHDRAWAL))
            onIntent(Intent.OnDescriptionChange(VALID_DESCRIPTION))
            onIntent(Intent.OnValueChange(VALID_VALUE))
            onIntent(Intent.OnDestinationAccountChange(VALID_ACCOUNT))
            onIntent(Intent.OnReserveChange(VALID_RESERVE))
        }

        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `validateFields for OTHER should require origin account`() {
        viewModel.run {
            onIntent(Intent.OnOperationTypeChange(OperationType.OTHER))
            onIntent(Intent.OnDescriptionChange(VALID_DESCRIPTION))
            onIntent(Intent.OnValueChange(VALID_VALUE))
            onIntent(Intent.OnOriginAccountChange(VALID_ACCOUNT))
            onIntent(Intent.OnDestinationAccountChange(VALID_DESTINATION_ACCOUNT))
        }

        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `onDateFilterChange should update currentDate`() {
        viewModel.onIntent(Intent.OnDateFilterChange(VALID_DATE))

        assertEquals(VALID_DATE, viewModel.uiState.value.currentDateFilter)
    }

    private companion object {
        const val EMPTY_STRING = ""
        const val VALID_DESCRIPTION = "Salary Payment"
        const val INVALID_VALUE = "abc"
        const val VALID_VALUE = "1000.50"
        val VALID_OPERATION_TYPE = OperationType.INCOME
        const val VALID_ACCOUNT = "My Bank"
        const val VALID_DESTINATION_ACCOUNT = "Savings Account"
        const val VALID_RESERVE = "Emergency Fund"
        const val VALID_TRANSACTION = "Test Transaction"
        const val SEARCH_QUERY = "reserve"
        val VALID_DATE: LocalDate = LocalDate.of(1998, 10, 20)
    }
}
