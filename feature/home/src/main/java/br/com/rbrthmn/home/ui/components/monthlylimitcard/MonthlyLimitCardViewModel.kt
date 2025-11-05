package br.com.rbrthmn.home.ui.components.monthlylimitcard

import br.com.rbrthmn.ui.utils.formatDouble
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class MonthlyLimitCardViewModel : MonthlyLimitCardContract.ViewModel() {
    override var uiState = MutableStateFlow(MonthlyLimitCardContract.UiState())

    override fun doOnInit(): MonthlyLimitCardViewModel {
        uiState.value = MonthlyLimitCardContract.UiState(
            monthLimit = formatDouble(MONTH_LIMIT_MOCK),
            monthDifference = formatDouble(MONTH_DIFFERENCE_MOCK)
        )

        return this
    }

    override fun onIntent(intent: MonthlyLimitCardContract.Intent) {
        when (intent) {
            is MonthlyLimitCardContract.Intent.OnDateFilterChange -> onDateFilterChange(intent.date)
        }
    }

    private fun onDateFilterChange(date: LocalDate) = uiState.update {
        it.copy(currentDateFilter = date)
    }

    companion object {
        const val MONTH_LIMIT_MOCK = 1000.0
        const val MONTH_DIFFERENCE_MOCK = 500.0
    }
}
