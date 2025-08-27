
package br.com.rbrthmn.home.ui

import br.com.rbrthmn.ui.BaseViewModel
import java.time.LocalDate

interface HomeScreenContract {
    abstract class ViewModel : BaseViewModel<UiState, Intent>()

    data class UiState(
        val currentDateFilter: LocalDate = LocalDate.now()
    )

    sealed class Intent {
        data class OnDateFilterChange(val date: LocalDate) : Intent()
    }
}
