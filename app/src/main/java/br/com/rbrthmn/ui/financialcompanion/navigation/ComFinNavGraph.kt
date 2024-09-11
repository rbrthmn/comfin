package br.com.rbrthmn.ui.financialcompanion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.com.rbrthmn.ui.financialcompanion.screens.HomeDestination
import br.com.rbrthmn.ui.financialcompanion.screens.HomeScreen
import br.com.rbrthmn.ui.financialcompanion.screens.IncomeDivisionsDestination
import br.com.rbrthmn.ui.financialcompanion.screens.IncomeDivisionsScreen
import br.com.rbrthmn.ui.financialcompanion.screens.MoreFeaturesDestination
import br.com.rbrthmn.ui.financialcompanion.screens.MoreFeaturesScreen
import br.com.rbrthmn.ui.financialcompanion.screens.OperationsDestination
import br.com.rbrthmn.ui.financialcompanion.screens.OperationsScreen

@Composable
fun ComFinNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = HomeDestination.route, modifier = modifier) {
        composable(route = HomeDestination.route) {
            HomeScreen(navController = navController)
        }
        composable(route = OperationsDestination.route) {
            OperationsScreen()
        }
        composable(route = MoreFeaturesDestination.route) {
            MoreFeaturesScreen(navController = navController)
        }
        composable(route = IncomeDivisionsDestination.route) {
            IncomeDivisionsScreen()
        }
    }
}