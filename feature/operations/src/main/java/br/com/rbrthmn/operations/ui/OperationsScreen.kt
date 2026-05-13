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
import br.com.rbrthmn.data.operations.model.OperationItem
import br.com.rbrthmn.data.operations.model.OperationsData
import br.com.rbrthmn.data.operations.repository.OperationsRepository
import br.com.rbrthmn.navigation.NavigationDestination
import br.com.rbrthmn.operations.ui.components.OperationsListCard
import br.com.rbrthmn.operations.ui.components.TotalBalanceCard
import br.com.rbrthmn.ui.R
import br.com.rbrthmn.ui.components.MonthSelectionTopBar
import br.com.rbrthmn.ui.utils.ResourceStringProvider
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

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
    OperationsScreen(viewModel = previewOperationsViewModel(context))
}

private fun previewOperationsViewModel(context: android.content.Context): OperationsScreenViewModel {
    val mockData = flowOf(
        OperationsData(
            operations = listOf(
                OperationItem(
                    id = 1,
                    date = LocalDate.now(),
                    counterparty = "Salary",
                    notes = null,
                    amount = 5000.0,
                    type = "INCOME",
                    category = "INCOME",
                    accountName = "Conta Principal"
                ),
                OperationItem(
                    id = 2,
                    date = LocalDate.now(),
                    counterparty = "Supermarket",
                    notes = null,
                    amount = 150.0,
                    type = "DEBIT_PURCHASE",
                    category = "DEBIT_PURCHASE",
                    accountName = "Conta Principal"
                ),
                OperationItem(
                    id = 3,
                    date = LocalDate.now().minusDays(1),
                    counterparty = "Transfer",
                    notes = null,
                    amount = 200.0,
                    type = "PIX",
                    category = "PIX",
                    accountName = "Conta B"
                ),
            ),
            totalIncome = 5000.0,
            totalOutcome = 350.0,
            totalBalance = 4650.0
        )
    )
    return OperationsScreenViewModel(
        stringProvider = ResourceStringProvider(context),
        operationsRepository = object : OperationsRepository {
            override fun getOperationsForMonth(year: Int, month: Int) = mockData
            override fun getAvailableAccounts() =
                flowOf(emptyList<br.com.rbrthmn.data.operations.model.AccountItem>())
            override fun getAvailableReserves() =
                flowOf(emptyList<br.com.rbrthmn.data.operations.model.ReserveItem>())
            override suspend fun addOperation(data: br.com.rbrthmn.data.operations.model.NewOperationData) =
                Result.success(Unit)
            override suspend fun deleteOperation(id: Long) = Result.success(Unit)
            override suspend fun updateOperation(id: Long, data: br.com.rbrthmn.data.operations.model.NewOperationData) =
                Result.success(Unit)
        }
    ).doOnInit()
}
