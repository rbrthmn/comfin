package br.com.rbrthmn.ui.financialcompanion.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.rbrthmn.ui.financialcompanion.components.SelectMonthTopBar
import br.com.rbrthmn.ui.financialcompanion.utils.ComFinNavigationType
import br.com.rbrthmn.ui.financialcompanion.utils.ContentType
import br.com.rbrthmn.ui.financialcompanion.utils.MonthsOfTheYear

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationsScreen(
    navigationType: ComFinNavigationType,
    contentType: ContentType,
    modifier: Modifier = Modifier
) {
    if (navigationType == ComFinNavigationType.BOTTOM_NAVIGATION && contentType == ContentType.LIST_ONLY) {
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
            OperationsScreenContent(innerPadding, modifier)
        }
    }
}


@Composable
private fun OperationsScreenContent(
    innerPaddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    val totalBalance = "R$ 1.000,00"
    val incoming = "R$ 2.000,00"
    val outflow = "R$ 1.000,00"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .padding(innerPaddingValues)
            .padding(horizontal = 20.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TotalBalanceCard(
            modifier = modifier,
            totalBalance = totalBalance,
            incoming = incoming,
            outflow = outflow
        )
    }
}

@Composable
fun TotalBalanceCard(
    modifier: Modifier = Modifier,
    totalBalance: String,
    incoming: String,
    outflow: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier.padding(top = 20.dp).shadow(elevation = 10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(vertical = 20.dp, horizontal = 20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.padding(bottom = 10.dp)
            ) {
                Text(text = totalBalance,
                    fontSize = 30.sp,)
                Text(
                    text = "Balanço", fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            HorizontalDivider(modifier)
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = incoming, fontSize = 20.sp)
                    Text(
                        text = "Entradas",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = outflow,  fontSize = 20.sp)
                    Text(
                        text = "Saídas", fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun OperationsScreenPreview(modifier: Modifier = Modifier) {
    OperationsScreen(
        navigationType = ComFinNavigationType.BOTTOM_NAVIGATION,
        contentType = ContentType.LIST_ONLY
    )
}

@Preview
@Composable
fun TotalBalanceCardPreview(modifier: Modifier = Modifier) {
    TotalBalanceCard(
        totalBalance = "R$ 1.000,00",
        incoming = "R$ 2.000,00",
        outflow = "R$ 1.000,00"
    )
}
