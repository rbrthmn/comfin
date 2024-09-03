package br.com.rbrthmn.ui.financialcompanion.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.components.SelectMonthTopBar
import br.com.rbrthmn.ui.financialcompanion.navigation.NavigationDestination
import br.com.rbrthmn.ui.financialcompanion.utils.MonthsOfTheYear
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random

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
    val operations = listOf<Operation>()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(topBar = {
        SelectMonthTopBar(
            currentMonth = MonthsOfTheYear.January,
            onCurrentMonthClick = { println("CURRENT MONTH") },
            onPreviousMonthClick = { println("PREVIOUS MONTH") },
            onNextMonthClick = { println("NEXT MONTH") },
            scrollBehavior = scrollBehavior,
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

@Composable
fun TotalBalanceCard(
    modifier: Modifier = Modifier,
    totalBalance: String,
    incomes: String,
    outflow: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier.shadow(elevation = dimensionResource(id = R.dimen.padding_small))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))
            ) {
                Text(
                    text = totalBalance,
                    fontSize = dimensionResource(id = R.dimen.font_size_large).value.sp,
                )
                Text(
                    text = stringResource(id = R.string.balance_title),
                    fontSize = dimensionResource(id = R.dimen.font_size_large).value.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            HorizontalDivider()
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.padding_small))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = incomes,
                        fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
                    )
                    Text(
                        text = stringResource(id = R.string.incomes_title),
                        fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = outflow,
                        fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
                    )
                    Text(
                        text = stringResource(id = R.string.outflow_title),
                        fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
fun OperationsListCard(operations: List<Operation>) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .padding(bottom = dimensionResource(id = R.dimen.padding_medium))
            .shadow(elevation = dimensionResource(id = R.dimen.padding_small))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(
                vertical = dimensionResource(id = R.dimen.padding_medium),
                horizontal = dimensionResource(id = R.dimen.padding_medium)
            )
        ) {
            TextField(
                value = "",
                onValueChange = { },
                label = { Text(text = stringResource(id = R.string.search_hint)) },
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalDivider()
            TextButton(
                onClick = {},
                contentPadding = PaddingValues(dimensionResource(id = R.dimen.zero_padding))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(id = R.string.add_icon_description),
                            tint = Color.Gray,
                            modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_extra_small))
                        )
                        Text(
                            text = stringResource(id = R.string.add_operation_button),
                            fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
                        )
                    }
                }
            }
            if (operations.isNotEmpty()) {
                HorizontalDivider()
                OperationsList(operations = operations)
            }
        }
    }
}

@Composable
fun OperationsList(operations: List<Operation>) {
    val groupedOperations = operations.groupBy {
        SimpleDateFormat("EEE, dd", Locale.getDefault()).format(it.date)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_small))
    ) {
        groupedOperations.forEach { (date, operationsForDate) ->
            DayOfWeekAndMonthText(date = operationsForDate[0].date)

            operationsForDate.forEach { operation ->
                OperationItem(
                    description = operation.description,
                    value = operation.value,
                    type = operation.type,
                    extras = operation.extras
                )
            }
        }
    }
}

@Composable
fun DayOfWeekAndMonthText(date: Date) {
    val formatter = SimpleDateFormat("EEE, dd", Locale.getDefault())
    val formattedDate = formatter.format(date)
    Text(
        text = formattedDate, fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
fun OperationItem(
    description: String,
    value: String,
    type: String,
    extras: String? = null
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = dimensionResource(id = R.dimen.padding_small))
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.height(IntrinsicSize.Min)
        ) {
            Text(
                text = description,
                fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = type,
                    fontSize = dimensionResource(id = R.dimen.font_size_small).value.sp
                )
                extras?.let {
                    VerticalDivider(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_extra_small)))
                    Text(
                        text = it,
                        fontSize = dimensionResource(id = R.dimen.font_size_small).value.sp
                    )
                }
            }
        }
        Text(text = value, fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp)
    }
}

@Preview
@Composable
fun OperationsScreenPreview(modifier: Modifier = Modifier) {
    fun Double.format(digits: Int) = "%.${digits}f".format(this)
    val operations = mutableListOf<Operation>()
    repeat(12) { index ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2023)

        if (index > 0 && Random.nextBoolean()) {
            calendar.time = operations[index - 1].date
        } else {
            calendar.set(Calendar.MONTH, Random.nextInt(12))
            calendar.set(Calendar.DAY_OF_MONTH, Random.nextInt(1, 29))
        }

        val types = listOf("Transferência", "Pagamento", "Depósito", "Saque")
        val extras = listOf("Conta A", "Conta B", "Cartão X", "Investimento Y", null)
        val value = Random.nextDouble(100.0, 5000.0).format(2)

        operations.add(
            Operation(
                description = "Teste ${index + 1}",
                value = value,
                date = calendar.time,
                type = types.random(),
                extras = extras.randomOrNull()
            )
        )
    }

    OperationsScreenContent(
        operations = operations,
        innerPaddingValues = PaddingValues(dimensionResource(id = R.dimen.zero_padding)),
    )
}

@Preview
@Composable
fun TotalBalanceCardPreview(modifier: Modifier = Modifier) {
    TotalBalanceCard(
        totalBalance = "R$ 1.000,00",
        incomes = "R$ 2.000,00",
        outflow = "R$ 1.000"
    )
}
