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

package br.com.rbrthmn.ui.financialcompanion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.rbrthmn.R
import br.com.rbrthmn.home.ui.HomeDestination
import br.com.rbrthmn.navigation.ComFinNavigationBar
import br.com.rbrthmn.navigation.ComFinNavigationType
import br.com.rbrthmn.navigation.NavigationItemContent
import br.com.rbrthmn.ui.financialcompanion.navigation.ComFinNavGraph
import br.com.rbrthmn.ui.financialcompanion.screens.morefeatures.MoreFeaturesDestination
import br.com.rbrthmn.ui.financialcompanion.screens.operations.OperationsDestination

const val NAV_GRAPH_TAG = "nav_graph"
const val NAV_BAR_TAG = "nav_bar"

@Composable
fun ComFinApp(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navigationType: ComFinNavigationType = when (windowSize) {
        WindowWidthSizeClass.Compact -> ComFinNavigationType.BOTTOM_NAVIGATION
        else -> ComFinNavigationType.BOTTOM_NAVIGATION
    }
    val currentDestination by navController.currentBackStackEntryAsState()
    val navItemsList = listOf(
        NavigationItemContent(
            icon = Icons.Default.Home,
            text = stringResource(id = R.string.home),
            route = HomeDestination.route
        ),
        NavigationItemContent(
            icon = Icons.Outlined.Menu,
            text = stringResource(id = R.string.operations),
            route = OperationsDestination.route
        ),
        NavigationItemContent(
            icon = Icons.Filled.MoreVert,
            text = stringResource(id = R.string.more),
            route = MoreFeaturesDestination.route
        )
    )

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier.background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            ComFinNavGraph(
                navController = navController,
                modifier = Modifier
                    .weight(0.92f)
                    .testTag(NAV_GRAPH_TAG)
            )
            ComFinNavigationBar(
                modifier = Modifier
                    .weight(0.08f)
                    .testTag(NAV_BAR_TAG),
                navigationItems = navItemsList,
                navigationType = navigationType,
                navigateToDestination = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },

                currentRoute = currentDestination?.destination?.route ?: HomeDestination.route
            )
        }
    }
}
