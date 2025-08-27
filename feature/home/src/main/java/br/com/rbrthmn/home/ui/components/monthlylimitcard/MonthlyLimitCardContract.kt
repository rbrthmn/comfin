
package br.com.rbrthmn.home.ui.components.monthlylimitcard

import br.com.rbrthmn.ui.BaseViewModel
import java.time.LocalDate

interface MonthlyLimitCardContract {
    abstract class ViewModel : BaseViewModel<UiState, Intent>()

    sealed class Intent {
        data class OnDateFilterChange(val date: LocalDate) : Intent()
    }

    data class UiState(
        val monthLimit: String = "",
        val monthDifference: String = "",
        val currentDateFilter: LocalDate = LocalDate.now()
    )
}