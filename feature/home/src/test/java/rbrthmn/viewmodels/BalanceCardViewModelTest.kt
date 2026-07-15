package rbrthmn.viewmodels

import androidx.compose.runtime.mutableStateOf
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardContract
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardContract.Intent
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardViewModel
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardViewModel.Companion.ACCOUNT_NAME_MOCK
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardViewModel.Companion.ACCOUNT_VALUE_MOCK
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardViewModel.Companion.BANK_NAME_MOCK
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardViewModel.Companion.TOTAL_BALANCE_MOCK
import br.com.rbrthmn.ui.R
import br.com.rbrthmn.ui.utils.formatDouble
import br.com.rbrthmn.ui.utils.formatString
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.util.Locale

class BalanceCardViewModelTest {
    private val viewModel = BalanceCardViewModel()

    @Test
    fun `doOnInit should display the total balance and account balances`() {
        viewModel.doOnInit()
        val expectedAccount = BalanceCardContract.BankAccountBalanceUiState(
            name = ACCOUNT_NAME_MOCK,
            value = formatDouble(ACCOUNT_VALUE_MOCK),
            bankName = BANK_NAME_MOCK,
        )

        assertEquals(formatDouble(TOTAL_BALANCE_MOCK), viewModel.uiState.value.totalBalance)
        assertEquals(expectedAccount.value, viewModel.uiState.value.accounts.first().value)
        assertEquals(expectedAccount.name, viewModel.uiState.value.accounts.first().name)
        assertEquals(expectedAccount.bankName, viewModel.uiState.value.accounts.first().bankName)
    }

    @Test
    fun `onSaveClick with valid input should add the new account and close the dialog`() {
        Locale.setDefault(Locale("pt", "BR"))

        assignValidInputs()
        val showDialog = mutableStateOf(true)

        val newAccount = BalanceCardContract.BankAccountBalanceUiState(
            name = VALID_STRING,
            value = formatString(VALID_BALANCE_STRING),
            bankName = VALID_STRING
        )
        val expectedAccounts = viewModel.uiState.value.accounts + newAccount

        viewModel.onIntent(Intent.OnSaveClick(showDialog))

        assertEquals(expectedAccounts, viewModel.uiState.value.accounts)
        assertEquals(false, showDialog.value)
        assertCleanedInputs()
    }

    @Test
    fun `onInitialBalanceChange with empty value should flag balance as required`() {
        viewModel.onIntent(Intent.OnInitialBalanceChange(EMPTY_STRING))

        assertEquals(false, viewModel.uiState.value.isNewAccountBalanceValid)
    }

    @Test
    fun `onInitialBalanceChange with valid value should accept the balance`() {
        viewModel.onIntent(Intent.OnInitialBalanceChange(VALID_BALANCE_STRING))

        assertEquals(true, viewModel.uiState.value.isNewAccountBalanceValid)
    }

    @Test
    fun `onDescriptionChange with empty value should flag description as required`() {
        viewModel.onIntent(Intent.OnDescriptionChange(EMPTY_STRING))

        assertEquals(false, viewModel.uiState.value.isNewAccountDescriptionValid)
    }

    @Test
    fun `onDescriptionChange with valid value should accept the description`() {
        viewModel.onIntent(Intent.OnDescriptionChange(VALID_STRING))

        assertEquals(true, viewModel.uiState.value.isNewAccountDescriptionValid)
    }

    @Test
    fun `onBankChange with empty bank should flag bank as required`() {
        viewModel.onIntent(Intent.OnBankChange(VALID_ID_STRING, EMPTY_STRING))

        assertEquals(false, viewModel.uiState.value.isNewAccountBankValid)
    }

    @Test
    fun `onBankChange with a bank selected should accept the bank`() {
        viewModel.onIntent(Intent.OnBankChange(VALID_ID_STRING, VALID_STRING))

        assertEquals(true, viewModel.uiState.value.isNewAccountBankValid)
    }

    @Test
    fun `cleanNewAccount should discard the new account form`() {
        assignValidInputs()

        viewModel.onIntent(Intent.CleanNewAccount)

        assertCleanedInputs()
    }

    @Test
    fun `onDateFilterChange should update the selected month`() {
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
        val VALID_DATE: LocalDate = LocalDate.of(1998, 10, 20)
    }
}
