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

package br.com.rbrthmn.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ComFinNavigationBar(
    modifier: Modifier = Modifier,
    navigationItems: List<NavigationItemContent>,
    navigationType: ComFinNavigationType,
    navigateToDestination: (String) -> Unit,
    currentRoute: String
) {
    when (navigationType) {
        ComFinNavigationType.BOTTOM_NAVIGATION -> {
            ComFinBottomNavigationBar(
                modifier = modifier.fillMaxWidth(),
                onItemPressed = navigateToDestination,
                navigationItemContentList = navigationItems,
                currentRoute = currentRoute
            )
        }
    }
}

@Composable
private fun ComFinBottomNavigationBar(
    modifier: Modifier = Modifier,
    onItemPressed: ((String) -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    currentRoute: String
) {
    NavigationBar(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = navItem.route == currentRoute,
                onClick = { onItemPressed(navItem.route) },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun ComFinNavigationBarPreview() {
    val navItemsList = listOf(
        NavigationItemContent(
            icon = Icons.Default.Home,
            text = "home",
            route = "home"
        ),
        NavigationItemContent(
            icon = Icons.Outlined.Menu,
            text = "operations",
            route = "operations"
        ),
        NavigationItemContent(
            icon = Icons.Filled.MoreVert,
            text = "more",
            route = "more"
        )
    )

    ComFinNavigationBar(
        navigationItems = navItemsList,
        navigationType = ComFinNavigationType.BOTTOM_NAVIGATION,
        navigateToDestination = {},
        currentRoute = "home"
    )
}
