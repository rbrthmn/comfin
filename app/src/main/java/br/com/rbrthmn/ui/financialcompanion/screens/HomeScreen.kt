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
import br.com.rbrthmn.ui.financialcompanion.components.Account
import br.com.rbrthmn.ui.financialcompanion.components.BalanceCard
import br.com.rbrthmn.ui.financialcompanion.components.CreditCardsBillCard
import br.com.rbrthmn.ui.financialcompanion.components.LastMonthDifferenceCard
import br.com.rbrthmn.ui.financialcompanion.components.MonthlyLimitCard
import br.com.rbrthmn.ui.financialcompanion.components.SelectMonthTopBar
import br.com.rbrthmn.ui.financialcompanion.navigation.NavigationDestination
import br.com.rbrthmn.ui.financialcompanion.utils.MonthsOfTheYear

object HomeDestination : NavigationDestination {
    override val route = "home"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMonthlyLimitCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(topBar = {
        SelectMonthTopBar(
            currentMonth = MonthsOfTheYear.JANUARY,
            onCurrentMonthClick = { println("CURRENT MONTH") },
            onPreviousMonthClick = { println("PREVIOUS MONTH") },
            onNextMonthClick = { println("NEXT MONTH") },
            scrollBehavior = scrollBehavior,
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        )
    }, modifier = modifier) { innerPadding ->
        HomeScreenContent(onMonthlyLimitCardClick, innerPadding, modifier)
    }
}

@Composable
private fun HomeScreenContent(
    onMonthlyLimitCardClick: () -> Unit,
    innerPaddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    val accounts = listOf(
        Account(1, "Banco A", "500,00"),
        Account(2, "Banco B", "1.000.000,00"),
        Account(3, "Banco C", "5,00")
    )
    val totalBalance = "1.000,00"

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
        BalanceCard(totalBalance = totalBalance, accounts = accounts)
        CreditCardsBillCard(totalCreditCardsBill = "2300,00", cardBills = accounts)
        LastMonthDifferenceCard("-5435,99")
    }
}

@Preview
@Composable
private fun HomeScreenPreview(modifier: Modifier = Modifier) {
    HomeScreen(onMonthlyLimitCardClick = {}, modifier = modifier)
}
