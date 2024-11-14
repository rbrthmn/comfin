/*
 *
 *  * Copyright (C) 2022 The Android Open Source Project
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Modifications made by Roberto Kenzo Hamano, 2024
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 *
 */

package br.com.rbrthmn.ui.financialcompanion.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.components.OperationsListCard
import br.com.rbrthmn.ui.financialcompanion.components.MonthSelectionTopBar
import br.com.rbrthmn.ui.financialcompanion.components.TotalBalanceCard
import br.com.rbrthmn.ui.financialcompanion.navigation.NavigationDestination
import br.com.rbrthmn.ui.financialcompanion.utils.MonthsOfTheYear
import br.com.rbrthmn.ui.financialcompanion.utils.getOperationsMock
import java.util.Date

data class Operation(
    val extras: String? = null,
    val description: String,
    val value: String,
    val date: Date,
    val type: String
)

object OperationsDestination : NavigationDestination {
    override val route = "operations"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationsScreen(
    modifier: Modifier = Modifier
) {
    val operations = getOperationsMock()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(topBar = {
        MonthSelectionTopBar(
            currentMonth = MonthsOfTheYear.JANUARY,
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        )
    }, modifier = modifier) { innerPadding ->
        OperationsScreenContent(operations, innerPadding, modifier)
    }
}


@Composable
private fun OperationsScreenContent(
    operations: List<Operation>,
    innerPaddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    val totalBalance = "R$ 1.000,00"
    val incomes = "R$ 2.000,00"
    val outflow = "R$ 1.000,00"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        modifier = modifier
            .padding(innerPaddingValues)
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TotalBalanceCard(
            modifier = modifier.padding(top = dimensionResource(id = R.dimen.padding_medium)),
            totalBalance = totalBalance,
            incomes = incomes,
            outflow = outflow
        )
        OperationsListCard(operations = operations.sortedByDescending { it.date })
    }
}

@Preview
@Composable
private fun OperationsScreenPreview(modifier: Modifier = Modifier) {
    OperationsScreenContent(
        operations = getOperationsMock(),
        innerPaddingValues = PaddingValues(dimensionResource(id = R.dimen.zero_padding)),
    )
}
