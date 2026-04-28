package br.com.rbrthmn.misc.incomedivisions

import br.com.rbrthmn.ui.BaseViewModel
import java.time.LocalDate

interface IncomeDivisionsScreenContract {
    abstract class ViewModel : BaseViewModel<UiState, Intent>()

    data class UiState(
        val incomeDivisions: List<IncomeDivision> = emptyList(),
        val showAddDivisionDialog: Boolean = false,
        val newDivisionName: String = "",
        val newDivisionValue: String = "",
        val newDivisionPercentage: String = "",
        val selectedDate: LocalDate = LocalDate.now()
    )

    sealed class Intent {
        object OnOpenAddDivisionDialog : Intent()
        object OnDismissAddDivisionDialog : Intent()
        data class OnNewDivisionNameChange(val name: String) : Intent()
        data class OnNewDivisionValueChange(val value: String) : Intent()
        data class OnNewDivisionPercentageChange(val percentage: String) : Intent()
        object OnSaveNewDivision : Intent()
        data class OnDateSelected(val date: LocalDate) : Intent()
    }
}
