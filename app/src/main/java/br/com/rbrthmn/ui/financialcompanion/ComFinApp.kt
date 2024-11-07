package br.com.rbrthmn.ui.financialcompanion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import br.com.rbrthmn.ui.financialcompanion.components.NavigationBar
import br.com.rbrthmn.ui.financialcompanion.navigation.ComFinNavGraph
import br.com.rbrthmn.ui.financialcompanion.utils.ComFinNavigationType

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

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier.background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            ComFinNavGraph(
                navController = navController,
                modifier = Modifier.weight(0.92f)
            )
            NavigationBar(
                modifier = Modifier.weight(0.08f),
                navigationType = navigationType,
                navigateToDestination = navController::navigate
            )
        }
    }
}

@Preview
@Composable
fun ComFinAppPreview(modifier: Modifier = Modifier) {
    ComFinApp(windowSize = WindowWidthSizeClass.Compact)
}