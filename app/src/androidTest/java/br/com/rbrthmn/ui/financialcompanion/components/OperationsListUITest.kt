/*
 *
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Modifications made by Roberto Kenzo Hamano, 2024
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package br.com.rbrthmn.ui.financialcompanion.components

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
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.screens.operations.OperationsScreenViewModel
import br.com.rbrthmn.ui.financialcompanion.screens.operations.components.ACCOUNTS_DROPDOWN_ICON_TAG
import br.com.rbrthmn.ui.financialcompanion.screens.operations.components.ACCOUNTS_DROPDOWN_MENU_TAG
import br.com.rbrthmn.ui.financialcompanion.screens.operations.components.NEW_OPERATION_DIALOG_TAG
import br.com.rbrthmn.ui.financialcompanion.screens.operations.components.OPERATION_TYPES_DROPDOWN_MENU_TAG
import br.com.rbrthmn.ui.financialcompanion.screens.operations.components.OperationsListCard
import br.com.rbrthmn.ui.onNodeWithStringId
import br.com.rbrthmn.ui.utils.ResourceStringProvider
import org.junit.Test

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
            .onNodeWithStringId(R.string.cancel_button)
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
            .onNodeWithStringId(R.string.save_button)
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
            onNodeWithContentDescription(activity.getString(R.string.drop_down_arrow_icon_description)).performClick()
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
            onNodeWithStringId(R.string.save_button).performClick()
            val dialog = onNodeWithTag(NEW_OPERATION_DIALOG_TAG)

            dialog.assertIsNotDisplayed()
        }
    }

    private companion object {
        const val VALID_DESCRIPTION = "description"
        const val VALID_VALUE = "100"
    }
}
