package br.com.rbrthmn.ui.financialcompanion.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.components.NavigationItemContent
import br.com.rbrthmn.ui.financialcompanion.components.SelectMonthTopBar
import br.com.rbrthmn.ui.financialcompanion.utils.ComfinNavigationType
import br.com.rbrthmn.ui.financialcompanion.utils.ContentType
import br.com.rbrthmn.ui.financialcompanion.utils.MonthsOfTheYear

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigationType: ComfinNavigationType,
    contentType: ContentType,
    modifier: Modifier = Modifier
) {
    val navigationItemContentList = listOf(
        NavigationItemContent(
            icon = Icons.Default.Home,
            text = stringResource(id = R.string.home)
        ),
        NavigationItemContent(
            icon = Icons.Default.Home,
            text = stringResource(id = R.string.operations)
        ),
        NavigationItemContent(
            icon = Icons.Default.Home,
            text = stringResource(id = R.string.dashboard)
        ),
        NavigationItemContent(
            icon = Icons.Default.Home,
            text = stringResource(id = R.string.more)
        )
    )

    if (navigationType == ComfinNavigationType.BOTTOM_NAVIGATION && contentType == ContentType.LIST_ONLY) {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

        Scaffold(topBar = {
            SelectMonthTopBar(
                currentMonth = MonthsOfTheYear.January,
                onCurrentMonthClick = { println("CURRENT MONTH") },
                onPreviousMonthClick = { println("PREVIOUS MONTH") },
                onNextMonthClick = { println("NEXT MONTH") },
                scrollBehavior = scrollBehavior,
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            )
        }, modifier = modifier) { innerPadding ->
            HomeScreenContent(innerPadding, modifier)
        }
    }
}

@Composable
private fun HomeScreenContent(innerPaddingValues: PaddingValues, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(innerPaddingValues)
            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
    ) {
        MonthlyLimitCard(modifier)
        BalanceCard(modifier)
    }
}

@Composable
private fun MonthlyLimitCard(modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .shadow(elevation = 10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp)
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
                    modifier = modifier
                        .padding(start = 5.dp)
                        .clickable { }
                )
            }
            Text(
                text = "R$ 1.000,00",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(bottom = 20.dp, top = 5.dp)
            )
            HorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .padding(horizontal = 10.dp)
                    .padding(top = 20.dp)
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
                text = "R$ 1.000,00",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(bottom = 20.dp, top = 5.dp)
            )
        }
    }
}

@Composable
private fun BalanceCard(modifier: Modifier = Modifier) {
    val accounts = listOf(Account(1,"Banco A", "500,00"), Account(2,"Banco B", "1.000,00"))
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .shadow(elevation = 10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(vertical = 20.dp, horizontal = 20.dp)
        ) {
            TotalBalanceText(modifier)
            HorizontalDivider(modifier.padding(vertical = 10.dp))
            BalanceList(modifier, accounts)
            AddBalanceAccountButton(modifier)
        }
    }
}

@Composable
private fun TotalBalanceText(modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "SALDO:",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "R$ 1.000,00",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun BalanceList(
    modifier: Modifier = Modifier,
    accounts: List<Account> = listOf()
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(accounts) { index, account ->
            if (index <= accounts.size) {
                BalanceItem(account.name, account.balance)
            }
        }
    }
}

@Composable
fun BalanceItem(accountName: String, accountBalance: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
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
        Text(text = "R$ $accountBalance", fontSize = 20.sp)
    }
}

@Composable
fun AddBalanceAccountButton(modifier: Modifier = Modifier) {
    TextButton(
        onClick = {  },
        contentPadding = PaddingValues(0.dp),
        modifier = modifier.fillMaxWidth())
    {
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
                Text(text = "Adicionar conta", fontSize = 20.sp)
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview(modifier: Modifier = Modifier) {
    HomeScreen(
        navigationType = ComfinNavigationType.BOTTOM_NAVIGATION,
        contentType = ContentType.LIST_ONLY,
        modifier = modifier
    )
}

data class Account(val id: Int, val name: String, val balance: String)