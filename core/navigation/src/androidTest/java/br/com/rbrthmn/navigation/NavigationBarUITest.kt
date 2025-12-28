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

package br.com.rbrthmn.navigation

import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import br.com.rbrthmn.ui.BaseUITest
import org.junit.Test

class NavigationBarUITest : BaseUITest() {
    override val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    override fun setup() = composeTestRule.setContent {
        navController = TestNavHostController(LocalContext.current)
        navController.navigatorProvider.addNavigator(ComposeNavigator())

        NavHost(
            navController = navController,
            startDestination = NAV_ITEM_ROUTE
        ) {
            composable(NAV_ITEM_ROUTE) {
            }
            composable(NAV_ITEM_ROUTE_2) {
            }
        }

        val navItemsList = listOf(
            NavigationItemContent(
                icon = Icons.Default.Home,
                text = NAV_ITEM_TEXT,
                route = NAV_ITEM_ROUTE
            ),
            NavigationItemContent(
                icon = Icons.Default.Home,
                text = NAV_ITEM_TEXT_2,
                route = NAV_ITEM_ROUTE_2
            )
        )

        ComFinNavigationBar(
            navigationItems = navItemsList,
            navigationType = ComFinNavigationType.BOTTOM_NAVIGATION,
            navigateToDestination = { route -> navController.navigate(route) },
            currentRoute = navController.currentDestination?.route ?: NAV_ITEM_ROUTE
        )
    }

    @Test
    fun navigationBar_displaysCorrectItems() {
        composeTestRule.run {
            onNodeWithContentDescription(NAV_ITEM_TEXT).assertIsDisplayed()
        }
    }

    @Test
    fun navigationBar_clickNavItem_shouldNavigateToItemRoute() {
        composeTestRule.run {
            onNodeWithContentDescription(NAV_ITEM_TEXT_2).performClick()

            assert(navController.currentBackStackEntry?.destination?.route == NAV_ITEM_ROUTE_2)
        }
    }

    private companion object {
        const val NAV_ITEM_TEXT = "item"
        const val NAV_ITEM_TEXT_2 = "item 2"
        const val NAV_ITEM_ROUTE = "route"
        const val NAV_ITEM_ROUTE_2 = "route 2"
    }
}
