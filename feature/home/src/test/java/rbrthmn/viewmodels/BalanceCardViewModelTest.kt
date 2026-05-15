package rbrthmn.viewmodels

import androidx.compose.runtime.mutableStateOf
import br.com.rbrthmn.data.home.model.AccountsSummary
import br.com.rbrthmn.data.home.model.AccountSummaryItem
import br.com.rbrthmn.data.home.model.CreditCardBillItem
import br.com.rbrthmn.data.home.repository.HomeRepository
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardContract
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardContract.Intent
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardViewModel
import br.com.rbrthmn.ui.R
import br.com.rbrthmn.ui.utils.formatDouble
import br.com.rbrthmn.ui.utils.formatString
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class BalanceCardViewModelTest {
    private val homeRepository: HomeRepository = mockk()
    private lateinit var viewModel: BalanceCardViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every { homeRepository.getAccountsSummary(any()) } returns flowOf(
            AccountsSummary(
                totalBalance = TOTAL_BALANCE,
                accounts = listOf(
                    AccountSummaryItem(
                        id = 1L,
                        name = ACCOUNT_NAME,
                        bankName = BANK_NAME,
                        balance = ACCOUNT_BALANCE,
                        isMainAccount = false
                    )
                )
            )
        )
        every { homeRepository.getCreditCardBills(any()) } returns flowOf(emptyList<CreditCardBillItem>())
        every { homeRepository.getLastMonthDifference(any()) } returns flowOf(0.0)
        every { homeRepository.getMonthlySpent(any()) } returns flowOf(0.0)
        viewModel = BalanceCardViewModel(homeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `doOnInit should collect accounts from repository and update state`() {
        viewModel.doOnInit()

        assertEquals(formatDouble(TOTAL_BALANCE), viewModel.uiState.value.totalBalance)
        assertEquals(ACCOUNT_NAME, viewModel.uiState.value.accounts.first().name)
        assertEquals(formatDouble(ACCOUNT_BALANCE), viewModel.uiState.value.accounts.first().value)
        assertEquals(BANK_NAME, viewModel.uiState.value.accounts.first().bankName)
    }

    @Test
    fun `onSaveClick with valid input should add new account`() {
        Locale.setDefault(Locale("pt", "BR"))

        assignValidInputs()
        val mock = mutableStateOf(true)

        val expectedFormattedValue = formatString(VALID_BALANCE_STRING)

        val newAccount = BalanceCardContract.BankAccountBalanceUiState(
            name = VALID_STRING,
            value = expectedFormattedValue,
            bankName = VALID_STRING
        )
        val newAccountList = viewModel.uiState.value.accounts + newAccount

        viewModel.onIntent(Intent.OnSaveClick(mock))

        assertEquals(newAccountList, viewModel.uiState.value.accounts)
    }

    @Test
    fun `onInitialBalanceChange with empty value should assign correctly`() {
        viewModel.onIntent(Intent.OnInitialBalanceChange(EMPTY_STRING))

        assertEquals(false, viewModel.uiState.value.isNewAccountBalanceValid)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newAccountBalance)
    }

    @Test
    fun `onInitialBalanceChange with value should assign correctly`() {
        viewModel.onIntent(Intent.OnInitialBalanceChange(VALID_BALANCE_STRING))

        assertEquals(true, viewModel.uiState.value.isNewAccountBalanceValid)
        assertEquals(VALID_BALANCE_STRING, viewModel.uiState.value.newAccountBalance)
    }

    @Test
    fun `onDescriptionChange with empty value should assign correctly`() {
        viewModel.onIntent(Intent.OnDescriptionChange(EMPTY_STRING))

        assertEquals(false, viewModel.uiState.value.isNewAccountDescriptionValid)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newAccountDescription)
    }

    @Test
    fun `onDescriptionChange with value should assign correctly`() {
        viewModel.onIntent(Intent.OnDescriptionChange(VALID_STRING))

        assertEquals(true, viewModel.uiState.value.isNewAccountDescriptionValid)
        assertEquals(VALID_STRING, viewModel.uiState.value.newAccountDescription)
    }

    @Test
    fun `onBankChange with empty value should assign correctly`() {
        viewModel.onIntent(Intent.OnBankChange(VALID_ID_STRING, EMPTY_STRING))

        assertEquals(false, viewModel.uiState.value.isNewAccountBankValid)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newAccountBank)
        assertEquals(VALID_ID_STRING, viewModel.uiState.value.newAccountBankIcon)
    }

    @Test
    fun `onBankChange with value should assign correctly`() {
        viewModel.onIntent(Intent.OnBankChange(VALID_ID_STRING, VALID_BALANCE_STRING))

        assertEquals(true, viewModel.uiState.value.isNewAccountBankValid)
        assertEquals(VALID_BALANCE_STRING, viewModel.uiState.value.newAccountBank)
        assertEquals(VALID_ID_STRING, viewModel.uiState.value.newAccountBankIcon)
    }

    @Test
    fun `cleanNewAccount should assign default values`() {
        viewModel.onIntent(Intent.CleanNewAccount)

        assertCleanedInputs()
    }

    @Test
    fun `onSaveClick with valid input should assign false to dialog`() {
        assignValidInputs()
        val mock = mutableStateOf(true)

        viewModel.onIntent(Intent.OnSaveClick(mock))

        assertEquals(false, mock.value)
    }

    @Test
    fun `onSaveClick with valid input should clean inputs`() {
        assignValidInputs()
        val mock = mutableStateOf(true)

        viewModel.onIntent(Intent.OnSaveClick(mock))

        assertCleanedInputs()
    }

    @Test
    fun `setDateFilter should assign value correctly`() {
        viewModel.onIntent(Intent.OnDateFilterChange(VALID_DATE))

        assertEquals(VALID_DATE, viewModel.uiState.value.currentDateFilter)
    }

    private fun assertCleanedInputs() {
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newAccountBalance)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newAccountDescription)
        assertEquals(EMPTY_STRING, viewModel.uiState.value.newAccountBank)
        assertEquals(R.drawable.bank_icon, viewModel.uiState.value.newAccountBankIcon)
        assertEquals(true, viewModel.uiState.value.isNewAccountBankValid)
        assertEquals(true, viewModel.uiState.value.isNewAccountDescriptionValid)
        assertEquals(true, viewModel.uiState.value.isNewAccountBalanceValid)
    }

    private fun assignValidInputs() {
        viewModel.run {
            onIntent(Intent.OnInitialBalanceChange(VALID_BALANCE_STRING))
            onIntent(Intent.OnDescriptionChange(VALID_STRING))
            onIntent(Intent.OnBankChange(VALID_ID_STRING, VALID_STRING))
        }
    }

    private companion object {
        const val VALID_BALANCE_STRING = "123"
        const val EMPTY_STRING = ""
        const val VALID_STRING = "test"
        const val VALID_ID_STRING = 1
        const val TOTAL_BALANCE = 2000.0
        const val ACCOUNT_BALANCE = 1000.0
        const val ACCOUNT_NAME = "Conta"
        const val BANK_NAME = "Meu Banco"
        val VALID_DATE: LocalDate = LocalDate.of(1998, 10, 20)
    }
}
