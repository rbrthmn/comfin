package br.com.rbrthmn.home.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import br.com.rbrthmn.home.R
import br.com.rbrthmn.home.ui.components.monthlylimitcard.MonthlyLimitCard
import br.com.rbrthmn.home.ui.components.monthlylimitcard.MonthlyLimitCardContract
import br.com.rbrthmn.home.ui.components.monthlylimitcard.MonthlyLimitCardViewModel
import br.com.rbrthmn.ui.BaseUITest
import br.com.rbrthmn.ui.onNodeWithStringId
import br.com.rbrthmn.ui.utils.DecimalFormatter
import br.com.rbrthmn.ui.utils.DecimalInputFieldFormatter
import io.mockk.mockk
import io.mockk.verify
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import java.time.LocalDate
import br.com.rbrthmn.ui.R as commonR

class MonthlyLimitCardUITest : BaseUITest() {
    override val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val mockOnCardClick = mockk<() -> Unit>(relaxed = true)

    override fun setup() = composeTestRule.setContent {
        MonthlyLimitCard(
            onCardClick = mockOnCardClick,
            currentDateFilter = LocalDate.now()
        )
    }

    @Test
    fun monthlyLimitCard_displaysCorrectTitles() {
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.monthly_limit_title))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.difference_title))
            .assertIsDisplayed()
    }

    @Test
    fun monthlyLimitCard_clickOnCard_shouldExecuteOnCardClickLambda() {
        val monthlyLimitTitle = composeTestRule.onNodeWithStringId(R.string.monthly_limit_title)

        monthlyLimitTitle.performClick()

        verify { mockOnCardClick() }
    }

    @Test
    fun monthlyLimitCard_clickOnMonthlyLimitHelpIcon_shouldShowDialog() {
        val monthlyLimitHelpIcon =
            composeTestRule.onAllNodesWithContentDescription(composeTestRule.activity.getString(R.string.help_icon_description))
                .onFirst()

        monthlyLimitHelpIcon.performClick()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.monthly_limit_dialog_text))
            .assertIsDisplayed()
    }

    @Test
    fun monthlyLimitCard_clickOnMonthlyDifferenceHelpIcon_shouldShowDialog() {
        val monthlyDifferenceHelpIcon =
            composeTestRule.onAllNodesWithContentDescription(composeTestRule.activity.getString(R.string.help_icon_description))
                .onLast()
        monthlyDifferenceHelpIcon.performClick()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.monthly_difference_dialog_text))
            .assertIsDisplayed()
    }

    @Test
    fun monthlyLimitCard_clickOnUnderstoodButton_shouldDismissDialog() {
        val monthlyLimitHelpIcon =
            composeTestRule.onAllNodesWithContentDescription(composeTestRule.activity.getString(R.string.help_icon_description))
                .onFirst()
        monthlyLimitHelpIcon.performClick()

        val understoodButton =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(commonR.string.understood_button))
        understoodButton.performClick()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.monthly_limit_dialog_text))
            .assertIsNotDisplayed()
    }

    private companion object {
        @JvmStatic
        @BeforeClass
        fun setupKoin() {
            startKoin {
                modules(
                    module {
                        single<DecimalInputFieldFormatter> { DecimalFormatter() }
                        viewModel<MonthlyLimitCardContract.ViewModel> {
                            MonthlyLimitCardViewModel().doOnInit()
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