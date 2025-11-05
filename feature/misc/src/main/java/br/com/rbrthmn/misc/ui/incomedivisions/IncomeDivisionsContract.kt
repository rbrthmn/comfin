package br.com.rbrthmn.misc.ui.incomedivisions

import br.com.rbrthmn.ui.BaseViewModel

abstract class IncomeDivisionsContract {

    abstract class ViewModel : BaseViewModel<UIState, Intent>()

    data class UIState(
        val incomeDivisions: List<IncomeDivision> = emptyList(),
        val showNewDivisionDialog: Boolean = false,
        val newDivisionName: String = "",
        val newDivisionValue: String = "",
        val newDivisionPercentage: String = "",
        val isNewDivisionNameValid: Boolean = true,
        val isNewDivisionValueValid: Boolean = true,
        val isNewDivisionPercentageValid: Boolean = true
    )

    sealed class Intent {
        data object OnAddDivisionButtonClick : Intent()
        data object OnCancelNewDivision : Intent()
        data object OnSaveNewDivision : Intent()
        data class OnNewDivisionNameChange(val name: String) : Intent()
        data class OnNewDivisionValueChange(val value: String) : Intent()
        data class OnNewDivisionPercentageChange(val percentage: String) : Intent()
        data class OnDivisionValueChanged(val index: Int, val value: String) : Intent()
        data class OnDivisionPercentageChanged(val index: Int, val percentage: String) : Intent()
    }
}
