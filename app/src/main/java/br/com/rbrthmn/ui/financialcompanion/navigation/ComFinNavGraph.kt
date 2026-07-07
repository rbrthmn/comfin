package br.com.rbrthmn.ui.financialcompanion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.com.rbrthmn.auth.passwordrecovery.PasswordRecoveryDestination
import br.com.rbrthmn.auth.passwordrecovery.PasswordRecoveryScreen
import br.com.rbrthmn.auth.signin.SignInDestination
import br.com.rbrthmn.auth.signin.SignInScreen
import br.com.rbrthmn.auth.signup.SignUpDestination
import br.com.rbrthmn.auth.signup.SignUpScreen
import br.com.rbrthmn.home.ui.HomeDestination
import br.com.rbrthmn.home.ui.HomeScreen
import br.com.rbrthmn.misc.incomedivisions.IncomeDivisionsDestination
import br.com.rbrthmn.misc.incomedivisions.IncomeDivisionsScreen
import br.com.rbrthmn.misc.morefeatures.MoreFeaturesDestination
import br.com.rbrthmn.misc.morefeatures.MoreFeaturesScreen
import br.com.rbrthmn.misc.recurringexpenses.RecurringExpenses
import br.com.rbrthmn.misc.recurringexpenses.RecurringExpensesDestination
import br.com.rbrthmn.misc.reserves.ReservesDestination
import br.com.rbrthmn.misc.reserves.ReservesScreen
import br.com.rbrthmn.operations.ui.OperationsDestination
import br.com.rbrthmn.operations.ui.OperationsScreen
import br.com.rbrthmn.settings.SettingsDestination
import br.com.rbrthmn.settings.SettingsScreen

@Composable
fun ComFinNavGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = SignInDestination.route) {
            SignInScreen(
                onNavigateToHome = {
                    navController.navigate(HomeDestination.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onNavigateToSignUp = { navController.navigate(SignUpDestination.route) },
                onNavigateToPasswordRecovery = {
                    navController.navigate(PasswordRecoveryDestination.route)
                }
            )
        }
        composable(route = SignUpDestination.route) {
            SignUpScreen(
                onNavigateToHome = {
                    navController.navigate(HomeDestination.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onNavigateToSignIn = { navController.popBackStack() }
            )
        }
        composable(route = PasswordRecoveryDestination.route) {
            PasswordRecoveryScreen(
                onNavigateToSignIn = { navController.popBackStack() }
            )
        }
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
