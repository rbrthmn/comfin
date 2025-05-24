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
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.navigation.ComFinNavGraph
import br.com.rbrthmn.ui.financialcompanion.navigation.NavigationBar
import br.com.rbrthmn.ui.financialcompanion.screens.home.HomeDestination
import br.com.rbrthmn.ui.financialcompanion.screens.morefeatures.MoreFeaturesDestination
import br.com.rbrthmn.ui.financialcompanion.screens.operations.OperationsDestination
import br.com.rbrthmn.ui.financialcompanion.utils.ComFinNavigationType
import org.junit.Test

class NavigationBarUITest : br.com.rbrthmn.ui.BaseUITest() {
    override val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    override fun setup() = composeTestRule.setContent {
        navController = TestNavHostController(LocalContext.current)
        navController.navigatorProvider.addNavigator(ComposeNavigator())

        ComFinNavGraph(navController = navController)
        NavigationBar(
            navigationType = ComFinNavigationType.BOTTOM_NAVIGATION,
            navigateToDestination = { route -> navController.navigate(route) },
        )
    }

    @Test
    fun navigationBar_displaysCorrectItems() {
        composeTestRule.run {
            onNodeWithContentDescription(activity.getString(R.string.home)).assertIsDisplayed()
            onNodeWithContentDescription(activity.getString(R.string.operations)).assertIsDisplayed()
            onNodeWithContentDescription(activity.getString(R.string.more)).assertIsDisplayed()
        }
    }

    @Test
    fun navigationBar_clickOnHome_shouldNavigateToHome() {
        composeTestRule.run {
            onNodeWithContentDescription(activity.getString(R.string.home)).performClick()

            assert(navController.currentBackStackEntry?.destination?.route == HomeDestination.route)
        }
    }

    @Test
    fun navigationBar_clickOnOperations_shouldNavigateToOperations() {
        composeTestRule.run {
            onNodeWithContentDescription(activity.getString(R.string.operations)).performClick()

            assert(navController.currentBackStackEntry?.destination?.route == OperationsDestination.route)
        }
    }

    @Test
    fun navigationBar_clickOnMore_shouldNavigateToMore() {
        composeTestRule.run {
            onNodeWithContentDescription(activity.getString(R.string.more)).performClick()

            assert(navController.currentBackStackEntry?.destination?.route == MoreFeaturesDestination.route)
        }
    }
}