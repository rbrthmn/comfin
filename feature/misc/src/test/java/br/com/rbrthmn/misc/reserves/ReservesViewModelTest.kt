package br.com.rbrthmn.misc.reserves

import br.com.rbrthmn.ui.utils.canBeFormatted
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.update
import org.junit.Test
import kotlin.test.assertEquals

class ReservesViewModelTest {
    private val viewModel = ReservesViewModel()

    @Test
    fun `doOnInit should assign initial values`() {
        viewModel.doOnInit()
        val expectedList = listOf(
            Reserve(
                name = "Emergency Fund",
                value = "1000.00",
                operations = listOf(
                    ReserveOperation(
                        date = "2023-11-15",
                        value = "200.00",
                        isWithdrawal = false
                    ),
                    ReserveOperation(
                        date = "2023-11-22",
                        value = "100.00",
                        isWithdrawal = true
                    ),
                    ReserveOperation(
                        date = "2023-12-01",
                        value = "300.00",
                        isWithdrawal = false
                    )
                )
            ),
            Reserve(
                name = "Birthday money",
                value = "100.00",
                operations = listOf()
            )
        )

        assertEquals(expectedList, viewModel.uiState.value.reserves)
    }

    @Test
    fun `onAddReserveButtonClick should update the ui`() {
        viewModel.onIntent(ReservesContract.Intent.OnAddReserveButtonClick)

        assertEquals(true, viewModel.uiState.value.showNewReserveDialog)
    }

    @Test
    fun `onCancelNewReserve should update the ui`() {
        viewModel.onIntent(ReservesContract.Intent.OnCancelNewReserve)

        assertEquals(false, viewModel.uiState.value.showNewReserveDialog)
        assertEquals("", viewModel.uiState.value.newReserveName)
        assertEquals("", viewModel.uiState.value.newReserveValue)
        assertEquals(true, viewModel.uiState.value.isNewReserveNameValid)
        assertEquals(true, viewModel.uiState.value.isNewReserveValueValid)
    }

    @Test
    fun `onSaveNewReserve should update reserve name and value validation ui state`() {
        val isNameValid = viewModel.uiState.value.newReserveName.isNotBlank()
        val isValueValid =
            viewModel.uiState.value.newReserveValue.isNotBlank() && canBeFormatted(viewModel.uiState.value.newReserveValue)

        viewModel.onIntent(ReservesContract.Intent.OnSaveNewReserve)

        assertEquals(isNameValid, viewModel.uiState.value.isNewReserveNameValid)
        assertEquals(isValueValid, viewModel.uiState.value.isNewReserveValueValid)
    }

    @Test
    fun `onSaveNewReserve with valid values should add new reserve`() {
        viewModel.uiState.update {
            it.copy(
                newReserveName = "name",
                newReserveValue = "100.00",
                isNewReserveNameValid = true,
                isNewReserveValueValid = true
            )
        }
        val newReserve = Reserve(
            name = "name",
            value = "100.00",
            operations = listOf()
        )
        val expectedReserves = viewModel.uiState.value.reserves + newReserve

        viewModel.onIntent(ReservesContract.Intent.OnSaveNewReserve)

        assertEquals(expectedReserves, viewModel.uiState.value.reserves)
        assertEquals(false, viewModel.uiState.value.showNewReserveDialog)
        assertEquals("", viewModel.uiState.value.newReserveName)
        assertEquals("", viewModel.uiState.value.newReserveValue)
        assertEquals(true, viewModel.uiState.value.isNewReserveNameValid)
        assertEquals(true, viewModel.uiState.value.isNewReserveValueValid)
    }

    @Test
    fun `onNewReserveNameChange should update the ui`() {
        viewModel.onIntent(ReservesContract.Intent.OnNewReserveNameChange("name"))

        assertEquals("name", viewModel.uiState.value.newReserveName)
        assertTrue(viewModel.uiState.value.isNewReserveNameValid)
    }

    @Test
    fun `onNewReserveValueChange with valid value should update the ui`() {
        viewModel.onIntent(ReservesContract.Intent.OnNewReserveValueChange("100.00"))

        assertEquals("100.00", viewModel.uiState.value.newReserveValue)
        assertTrue(viewModel.uiState.value.isNewReserveValueValid)
    }

    @Test
    fun `onNewReserveValueChange with invalid value should update the ui`() {
        viewModel.onIntent(ReservesContract.Intent.OnNewReserveValueChange("value"))

        assertEquals("value", viewModel.uiState.value.newReserveValue)
        assertFalse(viewModel.uiState.value.isNewReserveValueValid)
    }

    @Test
    fun `onReserveItemClick should update the ui`() {
        viewModel.onIntent(ReservesContract.Intent.OnReserveItemClick("id"))

        assertEquals("id", viewModel.uiState.value.expandedReserveId)
    }
}