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

package br.com.rbrthmn.ui.financialcompanion.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.BalanceCard
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.CreditCardBillsCard
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.LastMonthDifferenceCard
import br.com.rbrthmn.ui.financialcompanion.commoncomponents.MonthSelectionTopBar
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.MonthlyLimitCard
import br.com.rbrthmn.ui.financialcompanion.navigation.NavigationDestination
import br.com.rbrthmn.ui.financialcompanion.utils.MonthsOfTheYear
import br.com.rbrthmn.ui.financialcompanion.utils.SnackBarProvider
import org.koin.compose.koinInject

object HomeDestination : NavigationDestination {
    override val route = "home"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMonthlyLimitCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackBarProvider: SnackBarProvider = koinInject()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarProvider.hostState) },
        topBar = {
            MonthSelectionTopBar(
                currentMonth = MonthsOfTheYear.JANUARY,
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            )
        }, modifier = modifier
    ) { innerPadding ->
        HomeScreenContent(onMonthlyLimitCardClick, innerPadding, modifier)
    }
}

@Composable
private fun HomeScreenContent(
    onMonthlyLimitCardClick: () -> Unit,
    innerPaddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        modifier = modifier
            .padding(innerPaddingValues)
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        MonthlyLimitCard(
            onCardClick = onMonthlyLimitCardClick,
            monthLimit = "1.000,00",
            monthDifference = "-5435,99"
        )
        BalanceCard()
        CreditCardBillsCard()
        LastMonthDifferenceCard("-5435,99")
    }
}

@Preview
@Composable
private fun HomeScreenPreview(modifier: Modifier = Modifier) {
    HomeScreen(onMonthlyLimitCardClick = {}, modifier = modifier)
}
