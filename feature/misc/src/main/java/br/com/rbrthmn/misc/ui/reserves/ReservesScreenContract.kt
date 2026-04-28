package br.com.rbrthmn.misc.reserves

import br.com.rbrthmn.ui.BaseViewModel

interface ReservesScreenContract {
    abstract class ViewModel : BaseViewModel<UiState, Intent>()

    data class UiState(
        val reserves: List<Reserve> = emptyList(),
        val totalReservesValue: String = "",
        val showAddReserveDialog: Boolean = false,
        val newReserveName: String = "",
        val newReserveValue: String = ""
    )

    sealed class Intent {
        object OnOpenAddReserveDialog : Intent()
        object OnDismissAddReserveDialog : Intent()
        data class OnNewReserveNameChange(val name: String) : Intent()
        data class OnNewReserveValueChange(val value: String) : Intent()
        object OnSaveNewReserve : Intent()
    }
}
