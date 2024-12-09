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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.screens.home.HomeDestination
import br.com.rbrthmn.ui.financialcompanion.screens.morefeatures.MoreFeaturesDestination
import br.com.rbrthmn.ui.financialcompanion.screens.operations.OperationsDestination
import br.com.rbrthmn.ui.financialcompanion.utils.ComFinNavigationType

@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    navigationType: ComFinNavigationType,
    navigateToDestination: (String) -> Unit
) {
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

    when (navigationType) {
        ComFinNavigationType.BOTTOM_NAVIGATION -> {
            ComFinBottomNavigationBar(
                onItemPressed = navigateToDestination,
                navigationItemContentList = navItemsList,
                modifier = modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ComFinBottomNavigationBar(
    onItemPressed: ((String) -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = false,
                onClick = {
                    onItemPressed(navItem.route)
                },
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
fun ComFinNavigationBarPreview(modifier: Modifier = Modifier) {
    NavigationBar(
        navigationType = ComFinNavigationType.BOTTOM_NAVIGATION,
        navigateToDestination = {})
}

data class NavigationItemContent(
    val icon: ImageVector,
    val text: String,
    val route: String
)
