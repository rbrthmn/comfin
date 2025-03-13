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
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.BaseUITest
import br.com.rbrthmn.ui.financialcompanion.screens.incomedivisions.IncomeDivisionsDestination
import br.com.rbrthmn.ui.financialcompanion.screens.morefeatures.FEATURES_LIST_TAG
import br.com.rbrthmn.ui.financialcompanion.screens.morefeatures.MoreFeaturesScreen
import br.com.rbrthmn.ui.financialcompanion.screens.recurringexpenses.RecurringExpensesDestination
import br.com.rbrthmn.ui.financialcompanion.screens.reserves.ReservesDestination
import br.com.rbrthmn.ui.financialcompanion.screens.settings.SettingsDestination
import org.junit.Test

class MoreFeaturesScreenUITest : BaseUITest() {
    override val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    override fun setup() = composeTestRule.setContent {
        MoreFeaturesScreen(onFeatureClick = { route ->
            clickedRoute = route
        })
    }

    private var clickedRoute: String? = null

    @Test
    fun more_features_list_should_have_correct_number_of_options() {
        val expectedNumberOfOptions = 4
        val featuresList = composeTestRule.onNodeWithTag(FEATURES_LIST_TAG).onChildren()

        featuresList.assertCountEquals(expectedNumberOfOptions)
    }

    @Test
    fun moreFeaturesList_hasCorrectLabels() {
        composeTestRule.run {
            onNodeWithText(composeTestRule.activity.getString(R.string.feature_label_reserves))
                .assertExists()
            onNodeWithText(composeTestRule.activity.getString(R.string.feature_label_recurring_expenses))
                .assertExists()
            onNodeWithText(composeTestRule.activity.getString(R.string.feature_label_income_distribution))
                .assertExists()
            onNodeWithText(composeTestRule.activity.getString(R.string.feature_label_settings))
                .assertExists()
        }
    }

    @Test
    fun on_reserves_feature_click_should_navigate_to_its_route() {
        composeTestRule.onNodeWithTag(ReservesDestination.route).performClick()
        val reservesButton = composeTestRule.onNodeWithTag(ReservesDestination.route)
        reservesButton.performClick()

        assert(clickedRoute == ReservesDestination.route)
    }

    @Test
    fun on_recurring_expenses_feature_click_should_navigate_to_its_route() {
        composeTestRule.onNodeWithTag(RecurringExpensesDestination.route).performClick()
        val reservesButton = composeTestRule.onNodeWithTag(RecurringExpensesDestination.route)
        reservesButton.performClick()

        assert(clickedRoute == RecurringExpensesDestination.route)
    }

    @Test
    fun on_income_divisions_feature_click_should_navigate_to_its_route() {
        composeTestRule.onNodeWithTag(IncomeDivisionsDestination.route).performClick()
        val reservesButton = composeTestRule.onNodeWithTag(IncomeDivisionsDestination.route)
        reservesButton.performClick()

        assert(clickedRoute == IncomeDivisionsDestination.route)
    }

    @Test
    fun on_settings_feature_click_should_navigate_to_its_route() {
        composeTestRule.onNodeWithTag(SettingsDestination.route).performClick()
        val reservesButton = composeTestRule.onNodeWithTag(SettingsDestination.route)
        reservesButton.performClick()

        assert(clickedRoute == SettingsDestination.route)
    }
}