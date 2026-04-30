/*
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
 */

package br.com.rbrthmn.misc

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import br.com.rbrthmn.misc.incomedivisions.IncomeDivisionsDestination
import org.junit.Test
import br.com.rbrthmn.ui.R as commonR

class IncomeDivisionsScreenUITest : br.com.rbrthmn.ui.BaseUITest() {
    override val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    override fun setup() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            navController.navigate(IncomeDivisionsDestination.route)
        }
    }

    @Test
    fun add_new_division_button_should_show_dialog() {
        val button =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.add_division))
        button.performClick()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.division_name_hint))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.division_value_hint))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.division_percentage_hint))
            .assertIsDisplayed()
    }

    @Test
    fun cancel_button_should_dismiss_dialog() {
        val addDivisionButton =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.add_division))
        addDivisionButton.performClick()

        val cancelButton =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(commonR.string.cancel_button))
        cancelButton.performClick()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.division_name_hint))
            .assertIsNotDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.division_value_hint))
            .assertIsNotDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.division_percentage_hint))
            .assertIsNotDisplayed()
    }

    @Test
    fun save_button_should_dismiss_dialog() {
        val addDivisionButton =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.add_division))
        addDivisionButton.performClick()

        val saveButton =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(commonR.string.save_button))
        saveButton.performClick()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.division_name_hint))
            .assertIsNotDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.division_value_hint))
            .assertIsNotDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.division_percentage_hint))
            .assertIsNotDisplayed()
    }
}
