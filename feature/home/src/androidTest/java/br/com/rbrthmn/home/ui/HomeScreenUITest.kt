package br.com.rbrthmn.home.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardContract
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardViewModel
import br.com.rbrthmn.home.ui.components.creditcardbillscard.CreditCardBillsCardContract
import br.com.rbrthmn.home.ui.components.creditcardbillscard.CreditCardBillsCardViewModel
import br.com.rbrthmn.home.ui.components.lastmonthdifferencecard.LastMonthDifferenceCardContract
import br.com.rbrthmn.home.ui.components.lastmonthdifferencecard.LastMonthDifferenceCardViewModel
import br.com.rbrthmn.home.ui.components.monthlylimitcard.MonthlyLimitCardContract
import br.com.rbrthmn.home.ui.components.monthlylimitcard.MonthlyLimitCardViewModel
import br.com.rbrthmn.ui.BaseUITest
import br.com.rbrthmn.ui.utils.DecimalFormatter
import br.com.rbrthmn.ui.utils.DecimalInputFieldFormatter
import br.com.rbrthmn.ui.utils.SnackBarProvider
import br.com.rbrthmn.ui.utils.SnackBarProviderImpl
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class HomeScreenUITest : BaseUITest() {
    override val composeTestRule: ComposeContentTestRule =
        createAndroidComposeRule<ComponentActivity>()

    override fun setup() = composeTestRule.setContent { HomeScreen(onMonthlyLimitCardClick = {}) }

    @Test
    fun homeScreen_should_haveFourCards() {
        val cards = composeTestRule.onNodeWithTag(HOME_SCREEN_CONTENT_TEST_TAG).onChildren()

        cards.assertCountEquals(EXPECTED_HOME_CARDS_COUNT)
    }

    private companion object {
        const val EXPECTED_HOME_CARDS_COUNT = 4

        @JvmStatic
        @BeforeClass
        fun setupKoin() {
            startKoin {
                modules(
                    module {
                        single<DecimalInputFieldFormatter> { DecimalFormatter() }
                        singleOf<SnackBarProvider>(::SnackBarProviderImpl)
                        viewModel<HomeScreenContract.ViewModel> {
                            HomeScreenViewModel().doOnInit()
                        }
                        viewModel<MonthlyLimitCardContract.ViewModel> {
                            MonthlyLimitCardViewModel().doOnInit()
                        }
                        viewModel<BalanceCardContract.ViewModel> {
                            BalanceCardViewModel().doOnInit()
                        }
                        viewModel<LastMonthDifferenceCardContract.ViewModel> {
                            LastMonthDifferenceCardViewModel().doOnInit()
                        }
                        viewModel<CreditCardBillsCardContract.ViewModel> {
                            CreditCardBillsCardViewModel().doOnInit()
                        }
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