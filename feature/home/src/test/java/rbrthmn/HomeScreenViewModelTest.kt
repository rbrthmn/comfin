package rbrthmn

import br.com.rbrthmn.home.ui.HomeScreenContract
import br.com.rbrthmn.home.ui.HomeScreenViewModel
import br.com.rbrthmn.ui.utils.DecimalFormatter
import br.com.rbrthmn.ui.utils.DecimalInputFieldFormatter
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import java.time.LocalDate
import kotlin.test.assertEquals

class HomeScreenViewModelTest {
    private val viewModel: HomeScreenContract.ViewModel = HomeScreenViewModel()

    @Test
    fun `onDateFilterChange should update the selected month`() {
        viewModel.onIntent(HomeScreenContract.Intent.OnDateFilterChange(VALID_DATE))

        assertEquals(VALID_DATE, viewModel.uiState.value.currentDateFilter)

    }

    private companion object {
        val VALID_DATE: LocalDate = LocalDate.of(1998, 10, 20)

        @JvmStatic
        @BeforeClass
        fun setupKoin() {
            startKoin {
                modules(
                    module {
                        single<DecimalInputFieldFormatter> { DecimalFormatter() }
                    }
                )
            }
        }

        @JvmStatic
        @AfterClass
        fun tearDownKoin() {
            stopKoin()
        }
    }
}