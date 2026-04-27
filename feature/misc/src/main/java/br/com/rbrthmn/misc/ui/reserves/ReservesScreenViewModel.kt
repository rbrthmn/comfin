package br.com.rbrthmn.misc.reserves

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ReservesScreenViewModel : ReservesScreenContract.ViewModel() {
    override var uiState = MutableStateFlow(ReservesScreenContract.UiState())

    override fun doOnInit(): ReservesScreenViewModel {
        val reserves = listOf(
            Reserve(
                name = "Emergency Fund",
                value = "1000.00",
                operations = listOf(
                    ReserveOperation(date = "2023-11-15", value = "200.00", isWithdrawal = false),
                    ReserveOperation(date = "2023-11-22", value = "100.00", isWithdrawal = true),
                    ReserveOperation(date = "2023-12-01", value = "300.00", isWithdrawal = false)
                )
            ),
            Reserve(name = "Birthday money", value = "100.00", operations = listOf())
        )
        uiState.value = ReservesScreenContract.UiState(
            reserves = reserves,
            totalReservesValue = computeTotal(reserves)
        )
        return this
    }

    override fun onIntent(intent: ReservesScreenContract.Intent) {
        when (intent) {
            ReservesScreenContract.Intent.OnOpenAddReserveDialog ->
                uiState.update { it.copy(showAddReserveDialog = true) }
            ReservesScreenContract.Intent.OnDismissAddReserveDialog ->
                dismissDialog()
            is ReservesScreenContract.Intent.OnNewReserveNameChange ->
                uiState.update { it.copy(newReserveName = intent.name) }
            is ReservesScreenContract.Intent.OnNewReserveValueChange ->
                uiState.update { it.copy(newReserveValue = intent.value) }
            ReservesScreenContract.Intent.OnSaveNewReserve ->
                onSaveNewReserve()
        }
    }

    private fun onSaveNewReserve() {
        with(uiState.value) {
            if (newReserveName.isBlank() || newReserveValue.isBlank()) return
            val newReserve = Reserve(name = newReserveName, value = newReserveValue, operations = listOf())
            val updatedReserves = reserves + newReserve
            uiState.update {
                it.copy(
                    reserves = updatedReserves,
                    totalReservesValue = computeTotal(updatedReserves),
                    showAddReserveDialog = false,
                    newReserveName = "",
                    newReserveValue = ""
                )
            }
        }
    }

    private fun dismissDialog() {
        uiState.update {
            it.copy(showAddReserveDialog = false, newReserveName = "", newReserveValue = "")
        }
    }

    private fun computeTotal(reserves: List<Reserve>): String =
        "%.2f".format(reserves.sumOf { it.value.toDoubleOrNull() ?: 0.0 })
}
