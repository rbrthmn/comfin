
package br.com.rbrthmn.home.ui.components.lastmonthdifferencecard

import br.com.rbrthmn.ui.utils.formatDouble
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class LastMonthDifferenceCardViewModel : LastMonthDifferenceCardContract.ViewModel() {
    override var uiState = MutableStateFlow(LastMonthDifferenceCardContract.UiState())

    override fun doOnInit(): LastMonthDifferenceCardViewModel {
        uiState.value = LastMonthDifferenceCardContract.UiState(formatDouble(MOCK))

        return this
    }

    override fun onIntent(intent: LastMonthDifferenceCardContract.Intent) {
        when (intent) {
            is LastMonthDifferenceCardContract.Intent.OnDateFilterChange -> setDateFilter(intent.date)
        }
    }

    private fun setDateFilter(date: LocalDate) = uiState.update {
        it.copy(currentDateFilter = date)
    }

    companion object {
        const val MOCK = -100.00
    }
}