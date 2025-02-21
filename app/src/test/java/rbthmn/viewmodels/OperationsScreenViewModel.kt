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

import androidx.compose.runtime.MutableState
import br.com.rbrthmn.model.OperationType
import br.com.rbrthmn.ui.financialcompanion.screens.operations.OperationsScreenViewModel
import br.com.rbrthmn.ui.financialcompanion.utils.StringProvider
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
    fun `onDescriptionChange with empty value should be invalid`() {
        viewModel.onDescriptionChange(EMPTY_STRING)

        assertFalse(viewModel.uiState.value.isNewOperationDescriptionValid)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newOperationDescription)
    }

    @Test
    fun `onDescriptionChange with valid value should be valid`() {
        viewModel.onDescriptionChange(VALID_DESCRIPTION)

        assertTrue(viewModel.uiState.value.isNewOperationDescriptionValid)
        assertEquals(VALID_DESCRIPTION, viewModel.uiState.value.newOperationDescription)
    }

    @Test
    fun `onValueChange with invalid format should be invalid`() {
        viewModel.onValueChange(INVALID_VALUE)

        assertFalse(viewModel.uiState.value.isNewOperationValueValid)
        assertEquals(INVALID_VALUE, viewModel.uiState.value.newOperationValue)
    }

    @Test
    fun `onValueChange with valid format should be valid`() {
        viewModel.onValueChange(VALID_VALUE)

        assertTrue(viewModel.uiState.value.isNewOperationValueValid)
        assertEquals(VALID_VALUE, viewModel.uiState.value.newOperationValue)
    }

    @Test
    fun `onOperationTypeChange should update type and be valid`() {
        viewModel.onOperationTypeChange(VALID_OPERATION_TYPE)

        assertTrue(viewModel.uiState.value.isNewOperationTypeValid)
        assertEquals(VALID_OPERATION_TYPE, viewModel.uiState.value.newOperationType)
    }

    @Test
    fun `onOperationTypeChange with TRANSFER should have 2 new operation fields`() {
        viewModel.onOperationTypeChange(OperationType.TRANSFER)
        val fields = viewModel.uiState.value.dialogFields
        
        assertEquals(2, fields.size)
    }
    
    @Test
    fun `onOperationTypeChange with DEBIT_PURCHASE should have 1 new operation field`() {
        viewModel.onOperationTypeChange(OperationType.DEBIT_PURCHASE)
        val fields = viewModel.uiState.value.dialogFields
        
        assertEquals(1, fields.size)
    }
    
    @Test
    fun `onOperationTypeChange with BILL_PAYMENT should have 1 new operation field`() {
        viewModel.onOperationTypeChange(OperationType.BILL_PAYMENT)
        val fields = viewModel.uiState.value.dialogFields
        
        assertEquals(1, fields.size)
    }

    @Test
    fun `onOperationTypeChange with WITHDRAWAL should have 1 new operation field`() {
        viewModel.onOperationTypeChange(OperationType.WITHDRAWAL)
        val fields = viewModel.uiState.value.dialogFields
        
        assertEquals(1, fields.size)
    }

    @Test
    fun `onOperationTypeChange with DEPOSIT should have 1 new operation field`() {
        viewModel.onOperationTypeChange(OperationType.DEPOSIT)
        val fields = viewModel.uiState.value.dialogFields
        
        assertEquals(1, fields.size)
    }

    @Test
    fun `onOperationTypeChange with INCOME should have 1 new operation field`() {
        viewModel.onOperationTypeChange(OperationType.INCOME)
        val fields = viewModel.uiState.value.dialogFields
        
        assertEquals(1, fields.size)
    }

    @Test
    fun `onOperationTypeChange with RESERVE_ALLOCATION should have 2 new operation fields`() {
        viewModel.onOperationTypeChange(OperationType.RESERVE_ALLOCATION)
        val fields = viewModel.uiState.value.dialogFields
        
        assertEquals(2, fields.size)
    }

    @Test
    fun `onOperationTypeChange with RESERVE_WITHDRAWAL should have 2 new operation fields`() {
        viewModel.onOperationTypeChange(OperationType.RESERVE_WITHDRAWAL)
        val fields = viewModel.uiState.value.dialogFields
        
        assertEquals(2, fields.size)
    }

    @Test
    fun `onOperationTypeChange with OTHER should have 1 new operation field`() {
        viewModel.onOperationTypeChange(OperationType.OTHER)
        val fields = viewModel.uiState.value.dialogFields
        
        assertEquals(1, fields.size)
    }

    @Test
    fun `onOriginAccountChange with valid account should update account if required`() {
        viewModel.onOperationTypeChange(OperationType.DEBIT_PURCHASE)
        viewModel.onOriginAccountChange(VALID_ACCOUNT)

        assertTrue(viewModel.uiState.value.isNewOperationOriginAccountValid)
        assertEquals(VALID_ACCOUNT, viewModel.uiState.value.newOperationOriginAccount)
    }

    @Test
    fun `onOriginAccountChange with invalid account should update account if required`() {
        viewModel.onOperationTypeChange(OperationType.DEBIT_PURCHASE)
        viewModel.onOriginAccountChange(EMPTY_STRING)

        assertFalse(viewModel.uiState.value.isNewOperationOriginAccountValid)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newOperationOriginAccount)
    }

    @Test
    fun `onDestinationAccountChange with invalid account should update account if required`() {
        viewModel.onOperationTypeChange(OperationType.DEPOSIT)
        viewModel.onDestinationAccountChange(EMPTY_STRING)

        assertFalse(viewModel.uiState.value.isNewOperationDestinationAccountValid)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newOperationDestinationAccount)
    }

    @Test
    fun `onDestinationAccountChange with valid account should update account if required`() {
        viewModel.onOperationTypeChange(OperationType.DEPOSIT)
        viewModel.onDestinationAccountChange(VALID_DESTINATION_ACCOUNT)

        assertTrue(viewModel.uiState.value.isNewOperationDestinationAccountValid)
        assertEquals(VALID_DESTINATION_ACCOUNT, viewModel.uiState.value.newOperationDestinationAccount)
    }

    @Test
    fun `onReserveChange should update reserve if required`() {
        viewModel.onOperationTypeChange(OperationType.RESERVE_ALLOCATION)
        viewModel.onReserveChange(VALID_RESERVE)

        assertTrue(viewModel.uiState.value.isNewOperationReserveValid)
        assertEquals(VALID_RESERVE, viewModel.uiState.value.newOperationReserve)
    }

    @Test
    fun `onSaveButtonClick with valid inputs should reset fields`() {
        viewModel.onDescriptionChange(VALID_TRANSACTION)
        viewModel.onValueChange(VALID_VALUE)
        viewModel.onOperationTypeChange(VALID_OPERATION_TYPE)
        viewModel.onDestinationAccountChange(VALID_DESTINATION_ACCOUNT)
        val mockDialog = mockk<MutableState<Boolean>>(relaxed = true)
        val expectedNewOperationsSize = viewModel.uiState.value.operations.size + 1

        viewModel.onSaveButtonClick(mockDialog)

        verify { mockDialog.value = false }
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newOperationDescription)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newOperationValue)
        assertEquals(expectedNewOperationsSize, viewModel.uiState.value.operations.size)
        assertNull(viewModel.uiState.value.newOperationType)
    }

    @Test
    fun `onSearchQueryChange with operation type query should filter operations`() = runBlocking {
        viewModel.onSearchQueryChange(SEARCH_QUERY)
        val filteredOperations = viewModel.uiState.first().operations

        assertTrue(filteredOperations.all {
            it.type.contains(
                SEARCH_QUERY,
                ignoreCase = true
            )
        })
    }

    @Test
    fun `onSearchQueryChange with operation description query should filter operations`() = runBlocking {
        viewModel.onSearchQueryChange(VALID_DESCRIPTION)
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
        viewModel.onSearchQueryChange(VALID_VALUE)
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
        viewModel.onSearchQueryChange(VALID_DESTINATION_ACCOUNT)
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
        viewModel.onDescriptionChange(EMPTY_STRING)

        assertFalse(viewModel.validateFields())
    }

    @Test
    fun `validateFields with invalid value format should be invalid`() {
        viewModel.onDescriptionChange(VALID_DESCRIPTION)
        viewModel.onValueChange(INVALID_VALUE)

        assertFalse(viewModel.validateFields())
    }

    @Test
    fun `validateFields with valid data should be valid`() {
        viewModel.onDescriptionChange(VALID_DESCRIPTION)
        viewModel.onValueChange(VALID_VALUE)
        viewModel.onOperationTypeChange(VALID_OPERATION_TYPE)
        viewModel.onDestinationAccountChange(VALID_DESTINATION_ACCOUNT)

        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `validateFields for TRANSFER should require both origin and destination accounts`() {
        viewModel.onDescriptionChange(VALID_DESCRIPTION)
        viewModel.onValueChange(VALID_VALUE)
        viewModel.onOperationTypeChange(OperationType.TRANSFER)
        viewModel.onOriginAccountChange(VALID_ACCOUNT)
        viewModel.onDestinationAccountChange(VALID_DESTINATION_ACCOUNT)

        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `validateFields for DEBIT_PURCHASE should require origin account`() {
        viewModel.onDescriptionChange(VALID_DESCRIPTION)
        viewModel.onValueChange(VALID_VALUE)
        viewModel.onOperationTypeChange(OperationType.DEBIT_PURCHASE)
        viewModel.onOriginAccountChange(VALID_ACCOUNT)

        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `validateFields for BILL_PAYMENT should require origin account`() {
        viewModel.onDescriptionChange(VALID_DESCRIPTION)
        viewModel.onValueChange(VALID_VALUE)
        viewModel.onOperationTypeChange(OperationType.BILL_PAYMENT)
        viewModel.onOriginAccountChange(VALID_ACCOUNT)

        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `validateFields for WITHDRAWAL should require origin account`() {
        viewModel.onDescriptionChange(VALID_DESCRIPTION)
        viewModel.onValueChange(VALID_VALUE)
        viewModel.onOperationTypeChange(OperationType.WITHDRAWAL)
        viewModel.onOriginAccountChange(VALID_ACCOUNT)

        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `validateFields for DEPOSIT should require origin account`() {
        viewModel.onDescriptionChange(VALID_DESCRIPTION)
        viewModel.onValueChange(VALID_VALUE)
        viewModel.onOperationTypeChange(OperationType.DEPOSIT)
        viewModel.onDestinationAccountChange(VALID_ACCOUNT)

        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `validateFields for INCOME should require origin account`() {
        viewModel.onDescriptionChange(VALID_DESCRIPTION)
        viewModel.onValueChange(VALID_VALUE)
        viewModel.onOperationTypeChange(OperationType.INCOME)
        viewModel.onDestinationAccountChange(VALID_ACCOUNT)

        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `validateFields for RESERVE_ALLOCATION should require reserve and origin account`() {
        viewModel.onDescriptionChange(VALID_DESCRIPTION)
        viewModel.onValueChange(VALID_VALUE)
        viewModel.onOperationTypeChange(OperationType.RESERVE_ALLOCATION)
        viewModel.onReserveChange(VALID_RESERVE)
        viewModel.onOriginAccountChange(VALID_ACCOUNT)

        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `validateFields for RESERVE_WITHDRAWAL should require reserve and destination account`() {
        viewModel.onDescriptionChange(VALID_DESCRIPTION)
        viewModel.onValueChange(VALID_VALUE)
        viewModel.onOperationTypeChange(OperationType.RESERVE_WITHDRAWAL)
        viewModel.onReserveChange(VALID_RESERVE)
        viewModel.onDestinationAccountChange(VALID_DESTINATION_ACCOUNT)

        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `validateFields for OTHER should require origin account`() {
        viewModel.onDescriptionChange(VALID_DESCRIPTION)
        viewModel.onValueChange(VALID_VALUE)
        viewModel.onOperationTypeChange(OperationType.OTHER)
        viewModel.onOriginAccountChange(VALID_ACCOUNT)

        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `onDateFilterChange should update currentDate`() {
        viewModel.onDateFilterChange(VALID_DATE)

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
        val VALID_DATE = LocalDate.of(1998, 10, 20)
    }
}
