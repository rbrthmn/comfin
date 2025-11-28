package br.com.rbrthmn.home.ui

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCard
import br.com.rbrthmn.home.ui.components.creditcardbillscard.CreditCardBillsCard
import br.com.rbrthmn.home.ui.components.lastmonthdifferencecard.LastMonthDifferenceCard
import br.com.rbrthmn.home.ui.components.monthlylimitcard.MonthlyLimitCard
import br.com.rbrthmn.navigation.NavigationDestination
import br.com.rbrthmn.ui.components.MonthSelectionTopBar
import br.com.rbrthmn.ui.utils.SnackBarProvider
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import java.time.LocalDate
import br.com.rbrthmn.ui.R as commonR

object HomeDestination : NavigationDestination {
    override val route = "home"
}

const val HOME_SCREEN_CONTENT_TEST_TAG = "home_screen_content"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onMonthlyLimitCardClick: () -> Unit,
    viewModel: HomeScreenContract.ViewModel = koinViewModel()
) {
    val snackBarProvider: SnackBarProvider = koinInject()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarProvider.hostState) },
        topBar = {
            MonthSelectionTopBar(
                initialDate = uiState.currentDateFilter,
                onDateSelected = {
                    viewModel.onIntent(
                        HomeScreenContract.Intent.OnDateFilterChange(it)
                    )
                },
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            )
        }, modifier = modifier
    ) { innerPadding ->
        HomeScreenContent(
            modifier,
            onMonthlyLimitCardClick,
            innerPadding,
            currentDate = uiState.currentDateFilter
        )
    }
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    onMonthlyLimitCardClick: () -> Unit,
    innerPaddingValues: PaddingValues,
    currentDate: LocalDate
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = commonR.dimen.padding_medium)),
        modifier = modifier
            .padding(innerPaddingValues)
            .padding(horizontal = dimensionResource(id = commonR.dimen.padding_medium))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .testTag(HOME_SCREEN_CONTENT_TEST_TAG)
    ) {
        MonthlyLimitCard(onCardClick = onMonthlyLimitCardClick, currentDateFilter = currentDate)
        BalanceCard(currentDateFilter = currentDate)
        CreditCardBillsCard(currentDateFilter = currentDate)
        LastMonthDifferenceCard(currentDateFilter = currentDate)
    }
}
