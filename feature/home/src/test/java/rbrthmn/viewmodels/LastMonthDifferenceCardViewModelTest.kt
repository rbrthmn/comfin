package rbrthmn.viewmodels

import br.com.rbrthmn.data.home.model.AccountsSummary
import br.com.rbrthmn.data.home.model.CreditCardBillItem
import br.com.rbrthmn.data.home.repository.HomeRepository
import br.com.rbrthmn.home.ui.components.lastmonthdifferencecard.LastMonthDifferenceCardContract
import br.com.rbrthmn.home.ui.components.lastmonthdifferencecard.LastMonthDifferenceCardViewModel
import br.com.rbrthmn.ui.utils.formatDouble
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class LastMonthDifferenceCardViewModelTest {
    private val homeRepository: HomeRepository = mockk()
    private lateinit var viewModel: LastMonthDifferenceCardViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every { homeRepository.getAccountsSummary(any()) } returns flowOf(AccountsSummary(0.0, emptyList()))
        every { homeRepository.getCreditCardBills(any()) } returns flowOf(emptyList<CreditCardBillItem>())
        every { homeRepository.getLastMonthDifference(any()) } returns flowOf(DIFFERENCE_VALUE)
        every { homeRepository.getMonthlySpent(any()) } returns flowOf(0.0)
        viewModel = LastMonthDifferenceCardViewModel(homeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `doOnInit should collect difference from repository and update state`() {
        viewModel.doOnInit()

        TestCase.assertEquals(formatDouble(DIFFERENCE_VALUE), viewModel.uiState.value.valueOfLastMonth)
    }

    @Test
    fun `setDateFilter should assign value correctly`() {
        viewModel.onIntent(LastMonthDifferenceCardContract.Intent.OnDateFilterChange(VALID_DATE))

        TestCase.assertEquals(VALID_DATE, viewModel.uiState.value.currentDateFilter)
    }

    private companion object {
        const val DIFFERENCE_VALUE = -100.0
        val VALID_DATE: LocalDate = LocalDate.of(1998, 10, 20)
    }
}
