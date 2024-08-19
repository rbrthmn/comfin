package br.com.rbrthmn.ui.financialcompanion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.rbrthmn.ui.financialcompanion.components.ComFinNavigationBar
import br.com.rbrthmn.ui.financialcompanion.screens.HomeScreen
import br.com.rbrthmn.ui.financialcompanion.utils.ComFinNavigationType
import br.com.rbrthmn.ui.financialcompanion.utils.ComFinScreens
import br.com.rbrthmn.ui.financialcompanion.utils.ContentType

@Composable
fun ComFinApp(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = ComFinScreens.valueOf(
        backStackEntry?.destination?.route ?: "HOME"
    )
    val navigationType: ComFinNavigationType
    val contentType: ContentType

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            navigationType = ComFinNavigationType.BOTTOM_NAVIGATION
            contentType = ContentType.LIST_ONLY
        }

        else -> {
            navigationType = ComFinNavigationType.BOTTOM_NAVIGATION
            contentType = ContentType.LIST_ONLY
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            NavHost(navController = navController, startDestination = "HOME", modifier.weight(0.9f)) {
                composable("HOME") {
                    HomeScreen(
                        contentType = contentType,
                        navigationType = navigationType
                    )
                }
            }
            ComFinNavigationBar(
                modifier = Modifier.weight(0.1f)
            )
        }
    }
}

@Preview
@Composable
fun ComFinAppPreview(modifier: Modifier = Modifier) {
    ComFinApp(windowSize = WindowWidthSizeClass.Compact)
}