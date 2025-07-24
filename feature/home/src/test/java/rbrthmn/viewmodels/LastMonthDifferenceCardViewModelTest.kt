package rbrthmn.viewmodels

import br.com.rbrthmn.home.ui.components.lastmonthdifferencecard.LastMonthDifferenceCardContract
import br.com.rbrthmn.home.ui.components.lastmonthdifferencecard.LastMonthDifferenceCardViewModel
import br.com.rbrthmn.ui.utils.formatDouble
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate

class LastMonthDifferenceCardViewModelTest {
    private val viewModel: LastMonthDifferenceCardContract.ViewModel =
        LastMonthDifferenceCardViewModel()

    @Test
    fun `doOnInit should assign initial values`() {
        viewModel.doOnInit()

        Assert.assertEquals(
            formatDouble(LastMonthDifferenceCardViewModel.Companion.MOCK),
            viewModel.uiState.value.valueOfLastMonth
        )
    }

    @Test
    fun `setDateFilter should assign value correctly`() {
        viewModel.onIntent(LastMonthDifferenceCardContract.Intent.OnDateFilterChange(VALID_DATE))

        TestCase.assertEquals(
            VALID_DATE,
            viewModel.uiState.value.currentDateFilter
        )
    }


    private companion object {
        val VALID_DATE: LocalDate = LocalDate.of(1998, 10, 20)
    }
}