
package br.com.rbrthmn.operations

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import br.com.rbrthmn.operations.ui.components.ACCOUNTS_DROPDOWN_MENU_ITEM_TAG
import br.com.rbrthmn.operations.ui.components.ADD_SIMPLE_ACCOUNT_DIALOG_TAG
import br.com.rbrthmn.operations.ui.components.AccountsDropdownMenu
import br.com.rbrthmn.operations.ui.components.OperationOriginAccount
import br.com.rbrthmn.ui.BaseUITest
import br.com.rbrthmn.ui.R
import org.junit.Test

class AccountsDropdownMenuUITest : BaseUITest() {
    override val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    override fun setup() = composeTestRule.setContent {
        AccountsDropdownMenu(
            operationAccountType = OperationOriginAccount,
            onAccountSelected = {},
            isError = false,
            accounts = accounts
        )
    }

    @Test
    fun arrow_icon_click_should_show_accounts_list() {
        composeTestRule.run {
            onNodeWithContentDescription(activity.getString(R.string.drop_down_arrow_icon_description)).performClick()

            onAllNodesWithTag(ACCOUNTS_DROPDOWN_MENU_ITEM_TAG).assertCountEquals(accounts.size + 1)
        }
    }

    @Test
    fun dropdown_menu_item_click_should_hide_accounts_list() {
        composeTestRule.run {
            onNodeWithContentDescription(activity.getString(R.string.drop_down_arrow_icon_description)).performClick()
            onAllNodesWithTag(ACCOUNTS_DROPDOWN_MENU_ITEM_TAG).onFirst().performClick()

            onAllNodesWithTag(ACCOUNTS_DROPDOWN_MENU_ITEM_TAG).assertCountEquals(0)
        }
    }

    @Test
    fun last_account_item_click_should_show_dialog() {
        composeTestRule.run {
            onNodeWithContentDescription(activity.getString(R.string.drop_down_arrow_icon_description)).performClick()
            onAllNodesWithTag(ACCOUNTS_DROPDOWN_MENU_ITEM_TAG).onLast().performClick()

            onNodeWithTag(ADD_SIMPLE_ACCOUNT_DIALOG_TAG).assertIsDisplayed()
        }
    }

    @Test
    fun close_icon_click_should_dismiss_dialog() {
        composeTestRule.run {
            onNodeWithContentDescription(activity.getString(R.string.drop_down_arrow_icon_description)).performClick()
            onAllNodesWithTag(ACCOUNTS_DROPDOWN_MENU_ITEM_TAG).onLast().performClick()
            onNodeWithContentDescription(activity.getString(R.string.close_icon_description)).performClick()

            onNodeWithTag(ADD_SIMPLE_ACCOUNT_DIALOG_TAG).assertIsNotDisplayed()
        }
    }

    @Test
    fun check_icon_click_should_dismiss_dialog() {
        composeTestRule.run {
            onNodeWithContentDescription(activity.getString(R.string.drop_down_arrow_icon_description)).performClick()
            onAllNodesWithTag(ACCOUNTS_DROPDOWN_MENU_ITEM_TAG).onLast().performClick()
            onNodeWithContentDescription(activity.getString(R.string.check_icon_description)).performClick()

            onNodeWithTag(ADD_SIMPLE_ACCOUNT_DIALOG_TAG).assertIsNotDisplayed()
        }
    }

    private companion object {
        val accounts = listOf("Conta A", "Conta B", "Conta C")
    }
}