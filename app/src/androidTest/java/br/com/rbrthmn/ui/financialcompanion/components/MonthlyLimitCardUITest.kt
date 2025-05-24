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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.navigation.ComFinNavGraph
import br.com.rbrthmn.ui.financialcompanion.screens.home.HomeDestination
import br.com.rbrthmn.ui.financialcompanion.screens.incomedivisions.IncomeDivisionsDestination
import br.com.rbrthmn.ui.onNodeWithStringId
import org.junit.Assert.assertEquals
import org.junit.Test

class MonthlyLimitCardUITest : br.com.rbrthmn.ui.BaseUITest() {
    override val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    override fun setup() = composeTestRule.setContent {
        navController = TestNavHostController(LocalContext.current)
        navController.navigatorProvider.addNavigator(ComposeNavigator())

        ComFinNavGraph(navController = navController)
        navController.navigate(HomeDestination.route)
    }


    @Test
    fun monthlyLimitCard_displaysCorrectTitles() {
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.monthly_limit_title))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.difference_title))
            .assertIsDisplayed()
    }

    @Test
    fun monthlyLimitCard_clickOnCard_shouldGoToIncomeDivisions() {
        val monthlyLimitTitle = composeTestRule.onNodeWithStringId(R.string.monthly_limit_title)

        monthlyLimitTitle.performClick()

        assertEquals(
            IncomeDivisionsDestination.route,
            navController.currentBackStackEntry?.destination?.route
        )
    }

    @Test
    fun monthlyLimitCard_clickOnMonthlyLimitHelpIcon_shouldShowDialog() {
        val monthlyLimitHelpIcon =
            composeTestRule.onAllNodesWithContentDescription(composeTestRule.activity.getString(R.string.help_icon_description))
                .onFirst()

        monthlyLimitHelpIcon.performClick()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.monthly_limit_dialog_text))
            .assertIsDisplayed()
    }

    @Test
    fun monthlyLimitCard_clickOnMonthlyDifferenceHelpIcon_shouldShowDialog() {
        val monthlyDifferenceHelpIcon =
            composeTestRule.onAllNodesWithContentDescription(composeTestRule.activity.getString(R.string.help_icon_description))
                .onLast()
        monthlyDifferenceHelpIcon.performClick()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.monthly_difference_dialog_text))
            .assertIsDisplayed()
    }

    @Test
    fun monthlyLimitCard_clickOnUnderstoodButton_shouldDismissDialog() {
        val monthlyLimitHelpIcon =
            composeTestRule.onAllNodesWithContentDescription(composeTestRule.activity.getString(R.string.help_icon_description))
                .onFirst()
        monthlyLimitHelpIcon.performClick()

        val understoodButton =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.understood_button))
        understoodButton.performClick()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.monthly_limit_dialog_text))
            .assertIsNotDisplayed()
    }
}