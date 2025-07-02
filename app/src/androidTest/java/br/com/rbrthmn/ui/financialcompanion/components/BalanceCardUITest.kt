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
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.balancecard.ADD_BANK_ACCOUNT_DIALOG_TAG
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.balancecard.BalanceCard
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.balancecard.BalanceCardViewModel
import org.junit.Test
import java.time.LocalDate

class BalanceCardUITest : br.com.rbrthmn.ui.BaseUITest() {
    override val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    override fun setup() = composeTestRule.setContent {
        BalanceCard(viewModel = BalanceCardViewModel(), currentDateFilter = LocalDate.now())
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

            val cancelButton = onNodeWithText(activity.getString(R.string.cancel_button))
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
            onNodeWithTag(br.com.rbrthmn.home.ui.components.BANKS_DROPDOWN_MENU_TAG).onChildren()
                .onFirst().performClick()
            val saveButton = onNodeWithText(activity.getString(R.string.save_button))
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
            onNodeWithTag(br.com.rbrthmn.home.ui.components.BANKS_DROPDOWN_MENU_TAG).onChildren()
                .onFirst().performClick()
            val saveButton = onNodeWithText(activity.getString(R.string.save_button))
            saveButton.performClick()

            onNodeWithTag(ADD_BANK_ACCOUNT_DIALOG_TAG).assertIsNotDisplayed()
        }
    }

    @Test
    fun balanceCard_clickOnSaveButton_withInvalidInputs_shouldNotAddAccount() {
        composeTestRule.run {
            val addAccountButton = onNodeWithText(activity.getString(R.string.add_account_button))
            addAccountButton.performClick()

            val saveButton = onNodeWithText(activity.getString(R.string.save_button))
            saveButton.performClick()

            onNodeWithText("New Account").assertIsNotDisplayed()
        }
    }

    @Test
    fun balanceCard_clickOnSaveButton_withInvalidInputs_shouldNotDismissDialog() {
        composeTestRule.run {
            val addAccountButton = onNodeWithText(activity.getString(R.string.add_account_button))
            addAccountButton.performClick()

            val saveButton = onNodeWithText(activity.getString(R.string.save_button))
            saveButton.performClick()

            onNodeWithTag(ADD_BANK_ACCOUNT_DIALOG_TAG).assertIsDisplayed()
        }
    }
}