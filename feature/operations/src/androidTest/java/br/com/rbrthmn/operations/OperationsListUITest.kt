package br.com.rbrthmn.operations

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import br.com.rbrthmn.operations.ui.OperationsScreenViewModel
import br.com.rbrthmn.operations.ui.components.ACCOUNTS_DROPDOWN_ICON_TAG
import br.com.rbrthmn.operations.ui.components.ACCOUNTS_DROPDOWN_MENU_TAG
import br.com.rbrthmn.operations.ui.components.NEW_OPERATION_DIALOG_TAG
import br.com.rbrthmn.operations.ui.components.OPERATION_TYPES_DROPDOWN_MENU_TAG
import br.com.rbrthmn.operations.ui.components.OperationsListCard
import br.com.rbrthmn.ui.onNodeWithStringId
import br.com.rbrthmn.ui.utils.ResourceStringProvider
import org.junit.Test
import br.com.rbrthmn.ui.R as uiR

class OperationsListUITest : br.com.rbrthmn.ui.BaseUITest() {
    override val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    override fun setup() {
        val stringProvider = ResourceStringProvider(composeTestRule.activity)
        val viewModel = OperationsScreenViewModel(stringProvider)
        composeTestRule.setContent { OperationsListCard(viewModel = viewModel) }
    }

    @Test
    fun add_new_button_should_show_dialog() {
        composeTestRule
            .onNodeWithStringId(R.string.add_operation_button)
            .performClick()
        val dialog = composeTestRule.onNodeWithTag(NEW_OPERATION_DIALOG_TAG)

        dialog.assertIsDisplayed()
    }

    @Test
    fun cancel_button_should_dismiss_dialog() {
        composeTestRule
            .onNodeWithStringId(R.string.add_operation_button)
            .performClick()
        composeTestRule
            .onNodeWithStringId(uiR.string.cancel_button)
            .performClick()
        val dialog = composeTestRule.onNodeWithTag(NEW_OPERATION_DIALOG_TAG)

        dialog.assertIsNotDisplayed()
    }

    @Test
    fun save_button_with_invalid_values_should_not_dismiss_dialog() {
        composeTestRule
            .onNodeWithStringId(R.string.add_operation_button)
            .performClick()
        composeTestRule
            .onNodeWithStringId(uiR.string.save_button)
            .performClick()
        val dialog = composeTestRule.onNodeWithTag(NEW_OPERATION_DIALOG_TAG)

        dialog.assertIsDisplayed()
    }

    @Test
    fun save_button_with_valid_values_should_dismiss_dialog() {
        composeTestRule.run {
            onNodeWithStringId(R.string.add_operation_button).performClick()
            onNodeWithStringId(R.string.operation_description_hint).performTextInput(
                VALID_DESCRIPTION
            )
            onNodeWithStringId(R.string.operation_value_hint).performTextInput(VALID_VALUE)
            onNodeWithContentDescription(activity.getString(uiR.string.drop_down_arrow_icon_description)).performClick()
            onNodeWithTag(OPERATION_TYPES_DROPDOWN_MENU_TAG)
                .onChildren()
                .onFirst()
                .performClick()
            onAllNodesWithTag(ACCOUNTS_DROPDOWN_ICON_TAG)
                .onFirst()
                .performClick()
            onAllNodesWithTag(ACCOUNTS_DROPDOWN_MENU_TAG)
                .onFirst()
                .onChildren()
                .onFirst()
                .performClick()
            onAllNodesWithTag(ACCOUNTS_DROPDOWN_ICON_TAG)
                .onLast()
                .performClick()
            onAllNodesWithTag(ACCOUNTS_DROPDOWN_MENU_TAG)
                .onLast()
                .onChildren()
                .onFirst()
                .performClick()
            onNodeWithStringId(uiR.string.save_button).performClick()
            val dialog = onNodeWithTag(NEW_OPERATION_DIALOG_TAG)

            dialog.assertIsNotDisplayed()
        }
    }

    private companion object {
        const val VALID_DESCRIPTION = "description"
        const val VALID_VALUE = "100"
    }
}