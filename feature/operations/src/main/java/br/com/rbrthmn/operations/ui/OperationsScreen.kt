package br.com.rbrthmn.operations.ui

import android.annotation.SuppressLint
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.rbrthmn.navigation.NavigationDestination
import br.com.rbrthmn.operations.ui.components.OperationsListCard
import br.com.rbrthmn.operations.ui.components.TotalBalanceCard
import br.com.rbrthmn.ui.R
import br.com.rbrthmn.ui.components.MonthSelectionTopBar
import br.com.rbrthmn.ui.utils.ResourceStringProvider
import org.koin.androidx.compose.koinViewModel

object OperationsDestination : NavigationDestination {
    override val route = "operations"
}

const val DATE_FILTER_TAG = "date_filter"
const val OPERATIONS_CARD_TAG = "operation_card"
const val TOTAL_BALANCE_CARD_TAG = "total_balance_card"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationsScreen(
    modifier: Modifier = Modifier,
    viewModel: OperationsScreenContract.ViewModel = koinViewModel(),
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(topBar = {
        MonthSelectionTopBar(
            initialDate = uiState.currentDateFilter,
            onDateSelected = {
                viewModel.onIntent(
                    OperationsScreenContract.Intent.OnDateFilterChange(it)
                )
            },
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .testTag(DATE_FILTER_TAG)
        )
    }, modifier = modifier) { innerPadding ->
        OperationsScreenContent(innerPadding, modifier, viewModel)
    }
}


@Composable
private fun OperationsScreenContent(
    innerPaddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: OperationsScreenContract.ViewModel,
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
        TotalBalanceCard(
            modifier = modifier
                .padding(top = dimensionResource(id = R.dimen.padding_medium))
                .testTag(TOTAL_BALANCE_CARD_TAG),
            uiState = viewModel.uiState.collectAsState().value
        )
        OperationsListCard(
            modifier = modifier.testTag(OPERATIONS_CARD_TAG),
            viewModel = viewModel
        )
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
private fun OperationsScreenPreview() {
    val context = LocalContext.current
    val stringProvider = ResourceStringProvider(context)

    OperationsScreen(viewModel = OperationsScreenViewModel(stringProvider).doOnInit())
}
