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
import br.com.rbrthmn.ui.financialcompanion.screens.RecurringExpenses
import br.com.rbrthmn.ui.financialcompanion.screens.RecurringExpensesDestination
import br.com.rbrthmn.ui.financialcompanion.screens.ReservesDestination
import br.com.rbrthmn.ui.financialcompanion.screens.ReservesScreen
import br.com.rbrthmn.ui.financialcompanion.screens.SettingsDestination
import br.com.rbrthmn.ui.financialcompanion.screens.SettingsScreen

@Composable
fun ComFinNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(onMonthlyLimitCardClick = { navController.navigate(route = IncomeDivisionsDestination.route) })
        }
        composable(route = OperationsDestination.route) {
            OperationsScreen()
        }
        composable(route = MoreFeaturesDestination.route) {
            MoreFeaturesScreen(onFeatureClick = navController::navigate)
        }
        composable(route = IncomeDivisionsDestination.route) {
            IncomeDivisionsScreen(onRecurringExpensesDivisionClick = {
                navController.navigate(
                    RecurringExpensesDestination.route
                )
            })
        }
        composable(route = ReservesDestination.route) {
            ReservesScreen()
        }
        composable(route = RecurringExpensesDestination.route) {
            RecurringExpenses()
        }
        composable(route = SettingsDestination.route) {
            SettingsScreen()
        }
    }
}