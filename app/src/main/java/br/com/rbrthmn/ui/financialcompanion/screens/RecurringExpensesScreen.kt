package br.com.rbrthmn.ui.financialcompanion.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.navigation.NavigationDestination

object RecurringExpensesDestination : NavigationDestination {
    override val route = "recurring_expenses"
}

@Composable
fun RecurringExpenses(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        RecurringExpensesCard()
    }
}

@Composable
fun RecurringExpensesCard(modifier: Modifier = Modifier) {

}
