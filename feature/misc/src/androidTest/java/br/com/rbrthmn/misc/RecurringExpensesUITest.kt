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
