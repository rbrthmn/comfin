package br.com.rbrthmn.home.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class HomeScreenViewModel : HomeScreenContract.ViewModel() {
    override val uiState = MutableStateFlow(HomeScreenContract.UiState())

    override fun doOnInit(): HomeScreenViewModel {
        return this
    }

    override fun onIntent(intent: HomeScreenContract.Intent) {
        when (intent) {
            is HomeScreenContract.Intent.OnDateFilterChange -> onDateFilterChange(intent.date)
        }
    }

    private fun onDateFilterChange(date: LocalDate) = uiState.update {
        it.copy(currentDateFilter = date)
    }
}
