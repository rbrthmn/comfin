
package br.com.rbrthmn.misc

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import br.com.rbrthmn.misc.recurringexpenses.NEW_RECURRING_EXPENSE_DIALOG_TAG
import br.com.rbrthmn.misc.recurringexpenses.RecurringExpenses
import br.com.rbrthmn.ui.onNodeWithStringId
import org.junit.Test
import br.com.rbrthmn.ui.R as commonR

class RecurringExpensesUITest : br.com.rbrthmn.ui.BaseUITest() {
    override val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    override fun setup() = composeTestRule.setContent { RecurringExpenses() }

    @Test
    fun add_new_button_should_show_dialog() {
        val button = composeTestRule.onNodeWithStringId(R.string.recurring_expense_add_button)
        button.performClick()

        val dialog = composeTestRule.onNodeWithTag(NEW_RECURRING_EXPENSE_DIALOG_TAG)
        dialog.assertIsDisplayed()
    }

    @Test
    fun cancel_button_should_dismiss_dialog() {
        composeTestRule.onNodeWithStringId(R.string.recurring_expense_add_button).performClick()

        val button = composeTestRule.onNodeWithStringId(commonR.string.cancel_button)
        button.performClick()

        val dialog = composeTestRule.onNodeWithTag(NEW_RECURRING_EXPENSE_DIALOG_TAG)
        dialog.assertIsNotDisplayed()
    }

    @Test
    fun save_button_should_dismiss_dialog() {
        composeTestRule.onNodeWithStringId(R.string.recurring_expense_add_button).performClick()

        val button = composeTestRule.onNodeWithStringId(commonR.string.save_button)
        button.performClick()

        val dialog = composeTestRule.onNodeWithTag(NEW_RECURRING_EXPENSE_DIALOG_TAG)
        dialog.assertIsNotDisplayed()
    }
}
