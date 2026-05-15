package br.com.rbrthmn.home.ui.components.monthlylimitcard

import androidx.lifecycle.viewModelScope
import br.com.rbrthmn.data.home.repository.HomeRepository
import br.com.rbrthmn.ui.utils.formatDouble
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class MonthlyLimitCardViewModel(private val homeRepository: HomeRepository) : MonthlyLimitCardContract.ViewModel() {
    override var uiState = MutableStateFlow(MonthlyLimitCardContract.UiState())
    private val dateFilter = MutableStateFlow(LocalDate.now())

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun doOnInit(): MonthlyLimitCardViewModel {
        uiState.update { it.copy(monthLimit = formatDouble(MONTH_LIMIT)) }
        viewModelScope.launch {
            dateFilter.flatMapLatest { date ->
                homeRepository.getMonthlySpent(date)
            }.collect { spent ->
                uiState.update { it.copy(monthDifference = formatDouble(spent)) }
            }
        }
        return this
    }

    override fun onIntent(intent: MonthlyLimitCardContract.Intent) {
        when (intent) {
            is MonthlyLimitCardContract.Intent.OnDateFilterChange -> onDateFilterChange(intent.date)
        }
    }

    private fun onDateFilterChange(date: LocalDate) {
        dateFilter.value = date
        uiState.update { it.copy(currentDateFilter = date) }
    }

    companion object {
        const val MONTH_LIMIT = 1000.0
    }
}
