package br.com.rbrthmn.misc

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import br.com.rbrthmn.misc.reserves.NEW_RESERVE_DIALOG_TAG
import br.com.rbrthmn.misc.reserves.ReservesScreen
import br.com.rbrthmn.ui.onNodeWithStringId
import org.junit.Test
import br.com.rbrthmn.ui.R as commonR

class ReservesUITest : br.com.rbrthmn.ui.BaseUITest() {
    override val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    override fun setup() = composeTestRule.setContent { ReservesScreen() }

    @Test
    fun add_new_button_should_show_dialog() {
        val button = composeTestRule.onNodeWithStringId(R.string.add_reserve_button)
        button.performClick()

        val dialog = composeTestRule.onNodeWithTag(NEW_RESERVE_DIALOG_TAG)
        dialog.assertIsDisplayed()
    }

    @Test
    fun cancel_button_should_dismiss_dialog() {
        composeTestRule.onNodeWithStringId(R.string.add_reserve_button).performClick()

        val button = composeTestRule.onNodeWithStringId(commonR.string.cancel_button)
        button.performClick()

        val dialog = composeTestRule.onNodeWithTag(NEW_RESERVE_DIALOG_TAG)
        dialog.assertIsNotDisplayed()
    }

    @Test
    fun save_button_should_dismiss_dialog() {
        composeTestRule.onNodeWithStringId(R.string.add_reserve_button).performClick()

        val button = composeTestRule.onNodeWithStringId(commonR.string.save_button)
        button.performClick()

        val dialog = composeTestRule.onNodeWithTag(NEW_RESERVE_DIALOG_TAG)
        dialog.assertIsNotDisplayed()
    }
}