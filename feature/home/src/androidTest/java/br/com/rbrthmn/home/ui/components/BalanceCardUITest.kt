package br.com.rbrthmn.home.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import br.com.rbrthmn.home.R
import br.com.rbrthmn.home.di.homeModule
import br.com.rbrthmn.home.ui.components.balancecard.ADD_BANK_ACCOUNT_DIALOG_TAG
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCard
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardViewModel
import br.com.rbrthmn.home.ui.components.balancecard.CANCEL_BUTTON_TAG
import br.com.rbrthmn.home.ui.components.balancecard.SAVE_BUTTON_TAG
import br.com.rbrthmn.ui.BaseUITest
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import java.time.LocalDate

class BalanceCardUITest : BaseUITest() {
    override val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    override fun setup() = composeTestRule.setContent {
        BalanceCard(
            viewModel = BalanceCardViewModel().doOnInit(),
            currentDateFilter = LocalDate.now()
        )
    }

    private companion object {
        @JvmStatic
        @BeforeClass
        fun setupKoin() {
            startKoin {
                modules(homeModule)
            }
        }

        @JvmStatic
        @AfterClass
        fun tearDownKoin() {
            stopKoin()
        }
    }

    @Test
    fun balanceCard_clickOnAddAccountButton_shouldShowDialog() {
        composeTestRule.run {
            val addAccountButton = onNodeWithText(activity.getString(R.string.add_account_button))

            addAccountButton.performClick()

            onNodeWithTag(ADD_BANK_ACCOUNT_DIALOG_TAG).assertIsDisplayed()
        }
    }

    @Test
    fun balanceCard_clickOnCancelButton_shouldDismissDialog() {
        composeTestRule.run {
            val addAccountButton = onNodeWithText(activity.getString(R.string.add_account_button))
            addAccountButton.performClick()

            val cancelButton = onNodeWithTag(CANCEL_BUTTON_TAG)
            cancelButton.performClick()

            onNodeWithTag(ADD_BANK_ACCOUNT_DIALOG_TAG).assertIsNotDisplayed()
        }
    }

    @Test
    fun balanceCard_clickOnSaveButton_withValidInputs_shouldAddAccount() {
        composeTestRule.run {
            val addAccountButton = onNodeWithText(activity.getString(R.string.add_account_button))
            addAccountButton.performClick()

            onNodeWithText(activity.getString(R.string.balance_name_input_hint)).performTextInput("New Account")
            onNodeWithText(activity.getString(R.string.balance_input_hint)).performTextInput("100.00")
            onNodeWithContentDescription(
                "${activity.getString(R.string.bank_hint)} ${
                    activity.getString(
                        R.string.drop_down_arrow_icon_description
                    )
                }"
            ).performClick()
            onNodeWithTag(BANKS_DROPDOWN_MENU_TAG).onChildren()
                .onFirst().performClick()
            val saveButton = onNodeWithTag(SAVE_BUTTON_TAG)
            saveButton.performClick()

            onNodeWithText("New Account").assertIsDisplayed()
        }
    }

    @Test
    fun balanceCard_clickOnSaveButton_withValidInputs_shouldDismissDialog() {
        composeTestRule.run {
            val addAccountButton = onNodeWithText(activity.getString(R.string.add_account_button))
            addAccountButton.performClick()
            onNodeWithText(activity.getString(R.string.balance_name_input_hint)).performTextInput("New Account")
            onNodeWithText(activity.getString(R.string.balance_input_hint)).performTextInput("100.00")
            onNodeWithContentDescription(
                "${activity.getString(R.string.bank_hint)} ${
                    activity.getString(
                        R.string.drop_down_arrow_icon_description
                    )
                }"
            ).performClick()
            onNodeWithTag(BANKS_DROPDOWN_MENU_TAG)
                .onChildren()
                .onFirst()
                .performClick()
            val saveButton = onNodeWithTag(SAVE_BUTTON_TAG)

            saveButton.performClick()

            onNodeWithTag(ADD_BANK_ACCOUNT_DIALOG_TAG).assertIsNotDisplayed()
        }
    }

    @Test
    fun balanceCard_clickOnSaveButton_withInvalidInputs_shouldNotAddAccount() {
        composeTestRule.run {
            val addAccountButton = onNodeWithText(activity.getString(R.string.add_account_button))
            addAccountButton.performClick()

            val saveButton = onNodeWithTag(SAVE_BUTTON_TAG)
            saveButton.performClick()

            onNodeWithText("New Account").assertIsNotDisplayed()
        }
    }

    @Test
    fun balanceCard_clickOnSaveButton_withInvalidInputs_shouldNotDismissDialog() {
        composeTestRule.run {
            val addAccountButton = onNodeWithText(activity.getString(R.string.add_account_button))
            addAccountButton.performClick()

            val saveButton = onNodeWithTag(SAVE_BUTTON_TAG)
            saveButton.performClick()

            onNodeWithTag(ADD_BANK_ACCOUNT_DIALOG_TAG).assertIsDisplayed()
        }
    }
}