package br.com.rbrthmn.misc.reserves

import br.com.rbrthmn.ui.BaseViewModel

interface ReservesContract {
    abstract class ViewModel : BaseViewModel<UIState, Intent>()

    data class UIState(
        val reserves: List<Reserve> = listOf(),
        val showNewReserveDialog: Boolean = false,
        val newReserveName: String = "",
        val newReserveValue: String = "",
        val expandedReserveId: String? = null
    )

    sealed class Intent {
        data class OnReserveClicked(val reserve: Reserve) : Intent()
        data object OnAddReserveButtonClick : Intent()
        data object OnCancelNewReserve : Intent()
        data object OnSaveNewReserve : Intent()
        data class OnNewReserveNameChange(val name: String) : Intent()
        data class OnNewReserveValueChange(val value: String) : Intent()
        data class OnReserveItemClick(val reserveId: String) : Intent()
    }
}
