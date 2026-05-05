package br.com.rbrthmn.operations

import androidx.compose.runtime.MutableState
import br.com.rbrthmn.data.operations.model.NewOperationData
import br.com.rbrthmn.data.operations.model.OperationItem
import br.com.rbrthmn.data.operations.model.OperationsData
import br.com.rbrthmn.data.operations.repository.OperationsRepository
import br.com.rbrthmn.operations.ui.OperationType
import br.com.rbrthmn.operations.ui.OperationsScreenContract.Intent
import br.com.rbrthmn.operations.ui.OperationsScreenViewModel
import br.com.rbrthmn.ui.utils.StringProvider
import br.com.rbrthmn.ui.utils.formatDouble
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class OperationsScreenViewModelTest {
    private lateinit var viewModel: OperationsScreenViewModel
    private val stringProvider: StringProvider = mockk(relaxed = true)
    private val operationsRepository: OperationsRepository = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every { operationsRepository.getOperationsForMonth(any(), any()) } returns flowOf(EMPTY_OPERATIONS_DATA)
        coEvery { operationsRepository.addOperation(any()) } returns Result.success(Unit)
        viewModel = OperationsScreenViewModel(
            stringProvider = stringProvider,
            operationsRepository = operationsRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `doOnInit should collect from repo and set empty state when no data`() {
        viewModel.doOnInit()

        assertEquals(formatDouble(0.0), viewModel.uiState.value.totalBalance)
        assertEquals(formatDouble(0.0), viewModel.uiState.value.totalIncome)
        assertEquals(formatDouble(0.0), viewModel.uiState.value.totalOutcome)
        assertTrue(viewModel.uiState.value.operations.isEmpty())
    }

    @Test
    fun `doOnInit should display operations from repo`() {
        every { operationsRepository.getOperationsForMonth(any(), any()) } returns flowOf(VALID_OPERATIONS_DATA)

        viewModel.doOnInit()

        assertEquals(1, viewModel.uiState.value.operations.size)
        assertEquals(VALID_OPERATION_ITEM.counterparty, viewModel.uiState.value.operations[0].description)
    }

    @Test
    fun `doOnInit should show totals from repo`() {
        every { operationsRepository.getOperationsForMonth(any(), any()) } returns flowOf(VALID_OPERATIONS_DATA)

        viewModel.doOnInit()

        assertEquals(formatDouble(1000.0), viewModel.uiState.value.totalIncome)
        assertEquals(formatDouble(500.0), viewModel.uiState.value.totalOutcome)
        assertEquals(formatDouble(500.0), viewModel.uiState.value.totalBalance)
    }

    @Test
    fun `onDateFilterChange should update currentDateFilter and re-collect operations`() {
        viewModel.doOnInit()

        viewModel.onIntent(Intent.OnDateFilterChange(VALID_DATE))

        assertEquals(VALID_DATE, viewModel.uiState.value.currentDateFilter)
        verify(exactly = 2) { operationsRepository.getOperationsForMonth(any(), any()) }
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
    fun `onOperationTypeChange with PIX should have 2 new operation fields`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.PIX))
        assertEquals(2, viewModel.uiState.value.dialogFields.size)
    }

    @Test
    fun `onOperationTypeChange with DEBIT_PURCHASE should have 1 new operation field`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.DEBIT_PURCHASE))
        assertEquals(1, viewModel.uiState.value.dialogFields.size)
    }

    @Test
    fun `onOperationTypeChange with BILL_PAYMENT should have 1 new operation field`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.BILL_PAYMENT))
        assertEquals(1, viewModel.uiState.value.dialogFields.size)
    }

    @Test
    fun `onOperationTypeChange with WITHDRAWAL should have 1 new operation field`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.WITHDRAWAL))
        assertEquals(1, viewModel.uiState.value.dialogFields.size)
    }

    @Test
    fun `onOperationTypeChange with DEPOSIT should have 1 new operation field`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.DEPOSIT))
        assertEquals(1, viewModel.uiState.value.dialogFields.size)
    }

    @Test
    fun `onOperationTypeChange with INCOME should have 1 new operation field`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.INCOME))
        assertEquals(1, viewModel.uiState.value.dialogFields.size)
    }

    @Test
    fun `onOperationTypeChange with RESERVE_CONTRIBUTION should have 2 new operation fields`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.RESERVE_CONTRIBUTION))
        assertEquals(2, viewModel.uiState.value.dialogFields.size)
    }

    @Test
    fun `onOperationTypeChange with RESERVE_REDEMPTION should have 2 new operation fields`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.RESERVE_REDEMPTION))
        assertEquals(2, viewModel.uiState.value.dialogFields.size)
    }

    @Test
    fun `onOperationTypeChange with OTHER should have 1 new operation field`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.OTHER))
        assertEquals(1, viewModel.uiState.value.dialogFields.size)
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
        assertEquals(VALID_DESTINATION_ACCOUNT, viewModel.uiState.value.newOperationDestinationAccount)
    }

    @Test
    fun `onReserveChange should update reserve if required`() {
        viewModel.onIntent(Intent.OnOperationTypeChange(OperationType.RESERVE_CONTRIBUTION))
        viewModel.onIntent(Intent.OnReserveChange(VALID_RESERVE))

        assertTrue(viewModel.uiState.value.isNewOperationReserveValid)
        assertEquals(VALID_RESERVE, viewModel.uiState.value.newOperationReserve)
    }

    @Test
    fun `onSaveButtonClick with valid inputs should call addOperation and reset fields`() {
        viewModel.run {
            onIntent(Intent.OnOperationTypeChange(VALID_OPERATION_TYPE))
            onIntent(Intent.OnDescriptionChange(VALID_DESCRIPTION))
            onIntent(Intent.OnValueChange(VALID_VALUE))
            onIntent(Intent.OnDestinationAccountChange(VALID_DESTINATION_ACCOUNT))
        }
        val mockDialog = mockk<MutableState<Boolean>>(relaxed = true)

        viewModel.onIntent(Intent.OnSaveButtonClick(mockDialog))

        coVerify { operationsRepository.addOperation(any<NewOperationData>()) }
        verify { mockDialog.value = false }
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newOperationDescription)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newOperationValue)
        assertNull(viewModel.uiState.value.newOperationType)
    }

    @Test
    fun `onSaveButtonClick should persist correct amount`() {
        viewModel.run {
            onIntent(Intent.OnOperationTypeChange(VALID_OPERATION_TYPE))
            onIntent(Intent.OnDescriptionChange(VALID_DESCRIPTION))
            onIntent(Intent.OnValueChange(VALID_VALUE))
            onIntent(Intent.OnDestinationAccountChange(VALID_DESTINATION_ACCOUNT))
        }
        val mockDialog = mockk<MutableState<Boolean>>(relaxed = true)

        viewModel.onIntent(Intent.OnSaveButtonClick(mockDialog))

        coVerify { operationsRepository.addOperation(match { it.amount == 1000.50 }) }
    }

    @Test
    fun `onSearchQueryChange with operation type query should filter operations`() = runBlocking {
        viewModel.onIntent(Intent.OnSearchQueryChange(SEARCH_QUERY))
        val filteredOperations = viewModel.uiState.first().operations

        assertTrue(filteredOperations.all { it.type.contains(SEARCH_QUERY, ignoreCase = true) })
    }

    @Test
    fun `onSearchQueryChange with operation description query should filter operations`() = runBlocking {
        viewModel.onIntent(Intent.OnSearchQueryChange(VALID_DESCRIPTION))
        val filteredOperations = viewModel.uiState.first().operations

        assertTrue(filteredOperations.all { it.description.contains(VALID_DESCRIPTION, ignoreCase = true) })
    }

    @Test
    fun `onSearchQueryChange with operation value query should filter operations`() = runBlocking {
        viewModel.onIntent(Intent.OnSearchQueryChange(VALID_VALUE))
        val filteredOperations = viewModel.uiState.first().operations

        assertTrue(filteredOperations.all { it.value.contains(VALID_VALUE, ignoreCase = true) })
    }

    @Test
    fun `onSearchQueryChange with operation extras query should filter operations`() = runBlocking {
        viewModel.onIntent(Intent.OnSearchQueryChange(VALID_DESTINATION_ACCOUNT))
        val filteredOperations = viewModel.uiState.first().operations

        assertTrue(filteredOperations.all { it.extras?.contains(VALID_DESTINATION_ACCOUNT, ignoreCase = true) == true })
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
    fun `validateFields for PIX should require both origin and destination accounts`() {
        viewModel.run {
            onIntent(Intent.OnOperationTypeChange(OperationType.PIX))
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
    fun `validateFields for DEPOSIT should require destination account`() {
        viewModel.run {
            onIntent(Intent.OnOperationTypeChange(OperationType.DEPOSIT))
            onIntent(Intent.OnDescriptionChange(VALID_DESCRIPTION))
            onIntent(Intent.OnValueChange(VALID_VALUE))
            onIntent(Intent.OnDestinationAccountChange(VALID_DESTINATION_ACCOUNT))
        }
        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `validateFields for INCOME should require destination account`() {
        viewModel.run {
            onIntent(Intent.OnOperationTypeChange(OperationType.INCOME))
            onIntent(Intent.OnDescriptionChange(VALID_DESCRIPTION))
            onIntent(Intent.OnValueChange(VALID_VALUE))
            onIntent(Intent.OnDestinationAccountChange(VALID_DESTINATION_ACCOUNT))
        }
        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `validateFields for RESERVE_CONTRIBUTION should require reserve and origin account`() {
        viewModel.run {
            onIntent(Intent.OnOperationTypeChange(OperationType.RESERVE_CONTRIBUTION))
            onIntent(Intent.OnDescriptionChange(VALID_DESCRIPTION))
            onIntent(Intent.OnValueChange(VALID_VALUE))
            onIntent(Intent.OnOriginAccountChange(VALID_ACCOUNT))
            onIntent(Intent.OnReserveChange(VALID_RESERVE))
        }
        assertTrue(viewModel.validateFields())
    }

    @Test
    fun `validateFields for RESERVE_REDEMPTION should require reserve and destination account`() {
        viewModel.run {
            onIntent(Intent.OnOperationTypeChange(OperationType.RESERVE_REDEMPTION))
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
        const val SEARCH_QUERY = "reserve"
        val VALID_DATE: LocalDate = LocalDate.of(1998, 10, 20)

        val EMPTY_OPERATIONS_DATA = OperationsData(
            operations = emptyList(),
            totalIncome = 0.0,
            totalOutcome = 0.0,
            totalBalance = 0.0
        )

        val VALID_OPERATION_ITEM = OperationItem(
            id = 1L,
            date = LocalDate.now(),
            counterparty = "Market",
            notes = null,
            amount = 50.0,
            type = "DEBIT_PURCHASE",
            category = "Food",
            accountName = null
        )

        val VALID_OPERATIONS_DATA = OperationsData(
            operations = listOf(VALID_OPERATION_ITEM),
            totalIncome = 1000.0,
            totalOutcome = 500.0,
            totalBalance = 500.0
        )
    }
}
