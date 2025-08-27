package br.com.rbrthmn.misc.reserves

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ReservesViewModel() : ReservesContract.ViewModel() {
    override val uiState = MutableStateFlow(ReservesContract.UIState())

    override fun doOnInit(): ReservesViewModel {
        uiState.update {
            it.copy(
                reserves = listOf(
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
            )
        }
        return this
    }

    override fun onIntent(intent: ReservesContract.Intent) {
        when (intent) {
            is ReservesContract.Intent.OnReserveClicked -> onReserveClicked(intent.reserve)
            is ReservesContract.Intent.OnAddReserveButtonClick -> onAddReserveButtonClick()
            is ReservesContract.Intent.OnCancelNewReserve -> onCancelNewReserve()
            is ReservesContract.Intent.OnSaveNewReserve -> onSaveNewReserve()
            is ReservesContract.Intent.OnNewReserveNameChange -> onNewReserveNameChange(intent.name)
            is ReservesContract.Intent.OnNewReserveValueChange -> onNewReserveValueChange(intent.value)
            is ReservesContract.Intent.OnReserveItemClick -> onReserveItemClick(intent.reserveId)
        }
    }

    private fun onReserveClicked(reserve: Reserve) {
        // Handle reserve click if needed
    }

    private fun onAddReserveButtonClick() {
        uiState.update { it.copy(showNewReserveDialog = true) }
    }

    private fun onCancelNewReserve() {
        uiState.update {
            it.copy(
                showNewReserveDialog = false,
                newReserveName = "",
                newReserveValue = ""
            )
        }
    }

    private fun onSaveNewReserve() {
        val currentState = uiState.value
        if (currentState.newReserveName.isNotBlank() && currentState.newReserveValue.isNotBlank()) {
            val newReserve = Reserve(
                name = currentState.newReserveName,
                value = currentState.newReserveValue,
                operations = listOf()
            )
            uiState.update {
                it.copy(
                    reserves = it.reserves + newReserve,
                    showNewReserveDialog = false,
                    newReserveName = "",
                    newReserveValue = ""
                )
            }
        }
    }

    private fun onNewReserveNameChange(name: String) {
        uiState.update { it.copy(newReserveName = name) }
    }

    private fun onNewReserveValueChange(value: String) {
        uiState.update { it.copy(newReserveValue = value) }
    }

    private fun onReserveItemClick(reserveId: String) {
        uiState.update {
            it.copy(
                expandedReserveId = if (it.expandedReserveId == reserveId) null else reserveId
            )
        }
    }
}
