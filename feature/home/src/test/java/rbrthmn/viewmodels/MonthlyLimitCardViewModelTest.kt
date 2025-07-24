package rbrthmn.viewmodels

import br.com.rbrthmn.home.ui.components.monthlylimitcard.MonthlyLimitCardContract
import br.com.rbrthmn.home.ui.components.monthlylimitcard.MonthlyLimitCardViewModel
import br.com.rbrthmn.ui.utils.formatDouble
import junit.framework.TestCase
import org.junit.Test
import java.time.LocalDate

class MonthlyLimitCardViewModelTest {
    private val viewModel: MonthlyLimitCardContract.ViewModel = MonthlyLimitCardViewModel()

    @Test
    fun `doOnInit should assign initial values`() {
        viewModel.doOnInit()

        TestCase.assertEquals(
            formatDouble(MonthlyLimitCardViewModel.Companion.MONTH_LIMIT_MOCK),
            viewModel.uiState.value.monthLimit
        )
        TestCase.assertEquals(
            formatDouble(MonthlyLimitCardViewModel.Companion.MONTH_DIFFERENCE_MOCK),
            viewModel.uiState.value.monthDifference
        )
    }

    @Test
    fun `setDateFilter should assign value correctly`() {
        viewModel.onIntent(MonthlyLimitCardContract.Intent.OnDateFilterChange(VALID_DATE))

        TestCase.assertEquals(
            VALID_DATE,
            viewModel.uiState.value.currentDateFilter
        )
    }

    private companion object {
        val VALID_DATE: LocalDate = LocalDate.of(1998, 10, 20)
    }
}