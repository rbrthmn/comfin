package br.com.rbrthmn.home.ui.components.lastmonthdifferencecard

import androidx.lifecycle.viewModelScope
import br.com.rbrthmn.data.home.repository.HomeRepository
import br.com.rbrthmn.ui.utils.formatDouble
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class LastMonthDifferenceCardViewModel(private val homeRepository: HomeRepository) : LastMonthDifferenceCardContract.ViewModel() {
    override var uiState = MutableStateFlow(LastMonthDifferenceCardContract.UiState())
    private val dateFilter = MutableStateFlow(LocalDate.now())

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun doOnInit(): LastMonthDifferenceCardViewModel {
        viewModelScope.launch {
            dateFilter.flatMapLatest { date ->
                homeRepository.getLastMonthDifference(date)
            }.collect { difference ->
                uiState.update { it.copy(valueOfLastMonth = formatDouble(difference)) }
            }
        }
        return this
    }

    override fun onIntent(intent: LastMonthDifferenceCardContract.Intent) {
        when (intent) {
            is LastMonthDifferenceCardContract.Intent.OnDateFilterChange -> setDateFilter(intent.date)
        }
    }

    private fun setDateFilter(date: LocalDate) {
        dateFilter.value = date
        uiState.update { it.copy(currentDateFilter = date) }
    }
}