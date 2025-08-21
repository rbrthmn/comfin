package br.com.rbrthmn.operations

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import br.com.rbrthmn.operations.ui.DATE_FILTER_TAG
import br.com.rbrthmn.operations.ui.OperationsScreen
import br.com.rbrthmn.operations.ui.TOTAL_BALANCE_CARD_TAG
import br.com.rbrthmn.ui.BaseUITest
import org.junit.Test

class OperationsUiTest : BaseUITest() {
    override val composeTestRule: ComposeContentTestRule =
        createAndroidComposeRule<ComponentActivity>()

    override fun setup() = composeTestRule.setContent { OperationsScreen() }

    @Test
    fun operationsScreen_should_have_date_filter_and_two_cards() {
        composeTestRule.run {
            onNodeWithTag(DATE_FILTER_TAG).assertIsDisplayed()
            onNodeWithTag(TOTAL_BALANCE_CARD_TAG).assertIsDisplayed()
        }
    }
}