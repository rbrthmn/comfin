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
import br.com.rbrthmn.home.ui.components.creditcardbillscard.ADD_CARD_DIALOG_TAG
import br.com.rbrthmn.home.ui.components.creditcardbillscard.BILL_CLOSE_DAY_DROPDOWN_MENU_TAG
import br.com.rbrthmn.home.ui.components.creditcardbillscard.CreditCardBillsCard
import br.com.rbrthmn.home.ui.components.creditcardbillscard.CreditCardBillsCardViewModel
import br.com.rbrthmn.ui.BaseUITest
import br.com.rbrthmn.ui.onNodeWithStringId
import br.com.rbrthmn.ui.utils.DecimalFormatter
import br.com.rbrthmn.ui.utils.DecimalInputFieldFormatter
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import java.time.LocalDate
import br.com.rbrthmn.ui.R as commonR

class CreditCardBillsCardUITest : BaseUITest() {
    override val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    override fun setup() {
        composeTestRule.setContent {
            CreditCardBillsCard(
                viewModel = CreditCardBillsCardViewModel().doOnInit(),
                currentDateFilter = LocalDate.now()
            )
        }
    }

    private companion object {
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

    @Test
    fun addItemButton_whenClicked_shouldShowDialog() {
        composeTestRule.run {
            onNodeWithStringId(R.string.add_card_button).performClick()

            onNodeWithTag(ADD_CARD_DIALOG_TAG).assertIsDisplayed()
        }
    }

    @Test
    fun clickOnCancelButton_shouldDismissDialog() {
        val addCardButton =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.add_card_button))
        addCardButton.performClick()

        val cancelButton =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(commonR.string.cancel_button))
        cancelButton.performClick()

        composeTestRule.onNodeWithTag(ADD_CARD_DIALOG_TAG).assertDoesNotExist()
    }

    @Test
    fun clickOnSaveButton_withValidInputs_shouldAddCard() {
        composeTestRule.run {
            val addCardButton = onNodeWithText(activity.getString(R.string.add_card_button))
            addCardButton.performClick()

            onNodeWithText(activity.getString(R.string.balance_name_input_hint)).performTextInput("New Card")
            onNodeWithText(activity.getString(R.string.card_bill_input_hint)).performTextInput("100.00")
            onNodeWithContentDescription(
                "${activity.getString(R.string.card_bill_close_day_hint)} ${activity.getString(R.string.drop_down_arrow_icon_description)}"
            ).performClick()
            onNodeWithTag(BILL_CLOSE_DAY_DROPDOWN_MENU_TAG).onChildren().onFirst().performClick()
            onNodeWithContentDescription(
                "${activity.getString(R.string.bank_hint)} ${activity.getString(R.string.drop_down_arrow_icon_description)}"
            ).performClick()
            onNodeWithTag(BANKS_DROPDOWN_MENU_TAG).onChildren()
                .onFirst().performClick()

            val saveButton = onNodeWithText(activity.getString(commonR.string.save_button))
            saveButton.performClick()

            onNodeWithText("New Card").assertIsDisplayed()
        }
    }

    @Test
    fun clickOnSaveButton_withValidInputs_shouldDismissDialog() {
        composeTestRule.run {
            val addCardButton = onNodeWithText(activity.getString(R.string.add_card_button))
            addCardButton.performClick()

            onNodeWithText(activity.getString(R.string.balance_name_input_hint)).performTextInput("New Card")
            onNodeWithText(activity.getString(R.string.card_bill_input_hint)).performTextInput("100.00")
            onNodeWithContentDescription(
                "${activity.getString(R.string.card_bill_close_day_hint)} ${activity.getString(R.string.drop_down_arrow_icon_description)}"
            ).performClick()
            onNodeWithTag(BILL_CLOSE_DAY_DROPDOWN_MENU_TAG).onChildren().onFirst().performClick()
            onNodeWithContentDescription(
                "${activity.getString(R.string.bank_hint)} ${activity.getString(R.string.drop_down_arrow_icon_description)}"
            ).performClick()
            onNodeWithTag(BANKS_DROPDOWN_MENU_TAG).onChildren()
                .onFirst().performClick()

            val saveButton = onNodeWithText(activity.getString(commonR.string.save_button))
            saveButton.performClick()

            onNodeWithTag(ADD_CARD_DIALOG_TAG).assertIsNotDisplayed()
        }
    }

    @Test
    fun clickOnSaveButton_withInValidInputs_shouldNotDismissDialog() {
        composeTestRule.run {
            val addCardButton = onNodeWithText(activity.getString(R.string.add_card_button))
            addCardButton.performClick()

            val saveButton = onNodeWithText(activity.getString(commonR.string.save_button))
            saveButton.performClick()

            onNodeWithTag(ADD_CARD_DIALOG_TAG).assertIsDisplayed()
        }
    }
}