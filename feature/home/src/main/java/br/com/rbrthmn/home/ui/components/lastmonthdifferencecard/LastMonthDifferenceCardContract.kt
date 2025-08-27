package br.com.rbrthmn.home.ui.components.lastmonthdifferencecard

import br.com.rbrthmn.ui.BaseViewModel
import java.time.LocalDate

interface LastMonthDifferenceCardContract {
    abstract class ViewModel : BaseViewModel<UiState, Intent>()

    sealed class Intent {
        data class OnDateFilterChange(val date: LocalDate) : Intent()
    }

    data class UiState(
        val valueOfLastMonth: String = "",
        val currentDateFilter: LocalDate = LocalDate.now()
    )
}