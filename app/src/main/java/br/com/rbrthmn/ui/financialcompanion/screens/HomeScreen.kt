package br.com.rbrthmn.ui.financialcompanion.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.components.SelectMonthTopBar
import br.com.rbrthmn.ui.financialcompanion.navigation.NavigationDestination
import br.com.rbrthmn.ui.financialcompanion.utils.MonthsOfTheYear

data class Account(val id: Int, val name: String, val balance: String)

object HomeDestination : NavigationDestination {
    override val route = "home"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
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
        HomeScreenContent(innerPadding, modifier)
    }
}

@Composable
private fun HomeScreenContent(innerPaddingValues: PaddingValues, modifier: Modifier = Modifier) {
    val accounts = listOf(
        Account(1, "Banco A", "R$ 500,00"),
        Account(2, "Banco B", "R$ 1.000,00"),
        Account(3, "Banco C", "R$ 5,00")
    )
    val totalBalance = "1.000,00"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .padding(innerPaddingValues)
            .padding(horizontal = 20.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        MonthlyLimitCard(monthLimit = "R$ 1.000,00", monthDifference = "-R$ 5435,99")
        TotalBalanceCard(totalBalance = totalBalance, accounts = accounts)
        CreditCardsBillCard(totalCreditCardsBill = "R$ 2300,00", cardBills = accounts)
        DifferenceFromLastMonthCard("-R$ 5435,99")
    }
}

@Composable
private fun MonthlyLimitCard(
    monthLimit: String,
    monthDifference: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .padding(top = 20.dp)
            .shadow(elevation = 10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "LIMITE DO MÊS",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Icon(
                    painter = painterResource(id = R.drawable.help),
                    contentDescription = "Ícone com ponto de interrogação",
                    tint = Color.Gray,
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clickable { }
                )
            }
            Text(
                text = monthLimit,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(bottom = 20.dp, top = 5.dp)
            )
            HorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.padding(top = 20.dp)
            ) {
                Text(
                    text = "DIFERENÇA",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Icon(
                    painter = painterResource(id = R.drawable.help),
                    contentDescription = "Ícone com ponto de interrogação",
                    tint = Color.Gray,
                    modifier = modifier
                        .padding(start = 5.dp)
                        .clickable { }
                )
            }
            Text(
                text = monthDifference,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(top = 5.dp)
            )
        }
    }
}

@Composable
private fun TotalBalanceCard(
    totalBalance: String,
    accounts: List<Account>,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier.shadow(elevation = 10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(20.dp)
        ) {
            TotalValueText(
                totalValueName = "SALDO:",
                totalValue = totalBalance,
                modifier = modifier
            )
            HorizontalDivider()
            ItemsList(modifier, accounts)
            AddItemButton(itemName = "conta")
        }
    }
}

@Composable
private fun TotalValueText(
    totalValueName: String,
    totalValue: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        Text(
            text = totalValueName,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = totalValue,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ItemsList(
    modifier: Modifier = Modifier,
    accounts: List<Account>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.padding(top = 10.dp)
    ) {
        for (account in accounts) {
            Item(account.name, account.balance)
        }
    }
}

@Composable
fun Item(accountName: String, accountBalance: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Ícone do banco",
                tint = Color.Gray,
                modifier = modifier
                    .padding(horizontal = 5.dp)
            )
            Text(text = accountName, fontSize = 20.sp)
        }
        Text(text = accountBalance, fontSize = 20.sp)
    }
}

@Composable
fun AddItemButton(itemName: String, modifier: Modifier = Modifier) {
    TextButton(
        onClick = { },
        contentPadding = PaddingValues(0.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Ícone de mais",
                    tint = Color.Gray,
                    modifier = modifier.padding(horizontal = 5.dp)
                )
                Text(text = "Adicionar $itemName", fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun CreditCardsBillCard(
    totalCreditCardsBill: String,
    cardBills: List<Account>,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier.shadow(elevation = 10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(vertical = 20.dp, horizontal = 20.dp)
        ) {
            TotalValueText(
                totalValueName = "FATURAS:",
                totalValue = totalCreditCardsBill,
                modifier = modifier
            )
            HorizontalDivider()
            ItemsList(modifier, cardBills)
            AddItemButton(itemName = "cartão", modifier)
        }
    }
}

@Composable
fun DifferenceFromLastMonthCard(differenceValue: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .padding(bottom = 20.dp)
            .shadow(elevation = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 20.dp)
        ) {
            Text(
                text = "Diferença do mês passado",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = modifier.weight(0.6f)
            )
            Row(horizontalArrangement = Arrangement.End, modifier = modifier.weight(0.4f)) {
                Text(text = differenceValue, fontSize = 20.sp)
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview(modifier: Modifier = Modifier) {
    HomeScreen(modifier = modifier)
}

@Preview
@Composable
fun MonthlyLimitCardPreview(modifier: Modifier = Modifier) {
    MonthlyLimitCard("323", "323")
}

@Preview
@Composable
fun BalanceCardPreview(modifier: Modifier = Modifier) {
    val accounts = listOf(Account(1, "Banco A", "500,00"), Account(2, "Banco B", "1.000,00"))
    val totalBalance = "1234.00"
    TotalBalanceCard(totalBalance = totalBalance, accounts = accounts, modifier = modifier)
}

@Preview
@Composable
fun CreditCardsBillCardPreview(modifier: Modifier = Modifier) {
    val accounts = listOf(Account(1, "Banco A", "500,00"), Account(2, "Banco B", "1.000,00"))
    val totalCreditCardsBill = "12234.00"
    CreditCardsBillCard(
        totalCreditCardsBill = totalCreditCardsBill,
        cardBills = accounts,
        modifier = modifier
    )
}

@Preview()
@Composable
fun DifferenceFromLastMonthCardPreview(modifier: Modifier = Modifier) {
    DifferenceFromLastMonthCard(differenceValue = "-4.00", modifier = modifier)
}
