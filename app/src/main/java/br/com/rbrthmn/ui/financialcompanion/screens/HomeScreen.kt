package br.com.rbrthmn.ui.financialcompanion.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
fun HomeScreenContent(innerPaddingValues: PaddingValues, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(innerPaddingValues)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        MonthLimitCard(modifier)
    }
}

@Composable
fun MonthLimitCard(modifier: Modifier = Modifier) {
    Card(colors = CardDefaults.cardColors(
        containerColor = Color.White),
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
                    modifier = modifier.padding(start = 5.dp).clickable {  }
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
                    modifier = modifier.padding(start = 5.dp).clickable {  }
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

@Preview
@Composable
fun HomeScreenPreview(modifier: Modifier = Modifier) {
    HomeScreen(
        navigationType = ComfinNavigationType.BOTTOM_NAVIGATION,
        contentType = ContentType.LIST_ONLY,
        modifier = modifier
    )
}
