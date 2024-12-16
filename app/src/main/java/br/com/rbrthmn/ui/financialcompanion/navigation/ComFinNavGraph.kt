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

package br.com.rbrthmn.ui.financialcompanion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.com.rbrthmn.ui.financialcompanion.screens.home.HomeDestination
import br.com.rbrthmn.ui.financialcompanion.screens.home.HomeScreen
import br.com.rbrthmn.ui.financialcompanion.screens.incomedivisions.IncomeDivisionsDestination
import br.com.rbrthmn.ui.financialcompanion.screens.incomedivisions.IncomeDivisionsScreen
import br.com.rbrthmn.ui.financialcompanion.screens.morefeatures.MoreFeaturesDestination
import br.com.rbrthmn.ui.financialcompanion.screens.morefeatures.MoreFeaturesScreen
import br.com.rbrthmn.ui.financialcompanion.screens.operations.OperationsDestination
import br.com.rbrthmn.ui.financialcompanion.screens.operations.OperationsScreen
import br.com.rbrthmn.ui.financialcompanion.screens.recurringexpenses.RecurringExpenses
import br.com.rbrthmn.ui.financialcompanion.screens.recurringexpenses.RecurringExpensesDestination
import br.com.rbrthmn.ui.financialcompanion.screens.reserves.ReservesDestination
import br.com.rbrthmn.ui.financialcompanion.screens.reserves.ReservesScreen
import br.com.rbrthmn.ui.financialcompanion.screens.settings.SettingsDestination
import br.com.rbrthmn.ui.financialcompanion.screens.settings.SettingsScreen

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