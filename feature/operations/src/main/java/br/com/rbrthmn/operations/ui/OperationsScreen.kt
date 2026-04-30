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
import br.com.rbrthmn.data.finance.model.BankAccount
import br.com.rbrthmn.data.finance.model.CreditCard
import br.com.rbrthmn.data.finance.model.Transaction
import br.com.rbrthmn.data.finance.repository.BankAccountRepository
import br.com.rbrthmn.data.finance.repository.CreditCardRepository
import br.com.rbrthmn.data.finance.repository.TransactionRepository
import br.com.rbrthmn.navigation.NavigationDestination
import br.com.rbrthmn.operations.ui.components.OperationsListCard
import br.com.rbrthmn.operations.ui.components.TotalBalanceCard
import br.com.rbrthmn.ui.R
import br.com.rbrthmn.ui.components.MonthSelectionTopBar
import br.com.rbrthmn.ui.utils.ResourceStringProvider
import kotlinx.coroutines.flow.flowOf
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
    OperationsScreen(viewModel = previewOperationsViewModel(context))
}

private fun previewOperationsViewModel(context: android.content.Context): OperationsScreenViewModel {
    val emptyTransactions = flowOf(emptyList<Transaction>())
    val emptyAccounts = flowOf(emptyList<BankAccount>())
    val emptyCards = flowOf(emptyList<CreditCard>())
    return OperationsScreenViewModel(
        stringProvider = ResourceStringProvider(context),
        transactionRepository = object : TransactionRepository {
            override fun getAll() = emptyTransactions
            override fun getByMonth(startEpochDay: Long, endEpochDay: Long) = emptyTransactions
            override suspend fun getById(id: Long): Transaction? = null
            override suspend fun insert(transaction: Transaction) = 0L
            override suspend fun update(transaction: Transaction) = 0
            override suspend fun delete(transaction: Transaction) = 0
        },
        bankAccountRepository = object : BankAccountRepository {
            override fun getAll() = emptyAccounts
            override suspend fun getById(id: Long): BankAccount? = null
            override suspend fun insert(account: BankAccount) = 0L
            override suspend fun update(account: BankAccount) = 0
            override suspend fun delete(account: BankAccount) = 0
        },
        creditCardRepository = object : CreditCardRepository {
            override fun getAll() = emptyCards
            override suspend fun getById(id: Long): CreditCard? = null
            override suspend fun insert(card: CreditCard) = 0L
            override suspend fun update(card: CreditCard) = 0
            override suspend fun delete(card: CreditCard) = 0
        }
    ).doOnInit()
}
