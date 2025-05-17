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

package br.com.rbrthmn.ui.financialcompanion.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.BaseUITest
import br.com.rbrthmn.ui.financialcompanion.onNodeWithStringId
import br.com.rbrthmn.ui.financialcompanion.screens.reserves.NEW_RESERVE_DIALOG_TAG
import br.com.rbrthmn.ui.financialcompanion.screens.reserves.ReservesScreen
import org.junit.Test

class ReservesUITest : BaseUITest() {
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

        val button = composeTestRule.onNodeWithStringId(R.string.cancel_button)
        button.performClick()

        val dialog = composeTestRule.onNodeWithTag(NEW_RESERVE_DIALOG_TAG)
        dialog.assertIsNotDisplayed()
    }

    @Test
    fun save_button_should_dismiss_dialog() {
        composeTestRule.onNodeWithStringId(R.string.add_reserve_button).performClick()

        val button = composeTestRule.onNodeWithStringId(R.string.save_button)
        button.performClick()

        val dialog = composeTestRule.onNodeWithTag(NEW_RESERVE_DIALOG_TAG)
        dialog.assertIsNotDisplayed()
    }
}