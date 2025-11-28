package br.com.rbrthmn.ui.financialcompanion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.com.rbrthmn.home.ui.HomeDestination
import br.com.rbrthmn.home.ui.HomeScreen
import br.com.rbrthmn.misc.ui.incomedivisions.IncomeDivisionsDestination
import br.com.rbrthmn.misc.ui.incomedivisions.IncomeDivisionsScreen
import br.com.rbrthmn.misc.ui.morefeatures.MoreFeaturesDestination
import br.com.rbrthmn.misc.ui.morefeatures.MoreFeaturesScreen
import br.com.rbrthmn.misc.ui.recurringexpenses.RecurringExpenses
import br.com.rbrthmn.misc.ui.recurringexpenses.RecurringExpensesDestination
import br.com.rbrthmn.misc.ui.reserves.ReservesDestination
import br.com.rbrthmn.misc.ui.reserves.ReservesScreen
import br.com.rbrthmn.operations.ui.OperationsDestination
import br.com.rbrthmn.operations.ui.OperationsScreen
import br.com.rbrthmn.settings.SettingsDestination
import br.com.rbrthmn.settings.SettingsScreen

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
            IncomeDivisionsScreen(
                onRecurringExpensesDivisionClick = {
                    navController.navigate(RecurringExpensesDestination.route)
                }
            )
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