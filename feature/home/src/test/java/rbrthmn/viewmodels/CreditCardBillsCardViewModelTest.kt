package rbrthmn.viewmodels

import androidx.compose.runtime.mutableStateOf
import br.com.rbrthmn.data.home.model.AccountsSummary
import br.com.rbrthmn.data.home.model.CreditCardBillItem
import br.com.rbrthmn.data.home.repository.HomeRepository
import br.com.rbrthmn.home.ui.components.creditcardbillscard.CreditCardBillsCardContract
import br.com.rbrthmn.home.ui.components.creditcardbillscard.CreditCardBillsCardViewModel
import br.com.rbrthmn.ui.R
import br.com.rbrthmn.ui.utils.formatDouble
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
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

@OptIn(ExperimentalCoroutinesApi::class)
class CreditCardBillsCardViewModelTest {
    private val homeRepository: HomeRepository = mockk()
    private lateinit var viewModel: CreditCardBillsCardViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every { homeRepository.getAccountsSummary(any()) } returns flowOf(AccountsSummary(0.0, emptyList()))
        every { homeRepository.getCreditCardBills(any()) } returns flowOf(
            listOf(
                CreditCardBillItem(
                    id = 1L,
                    name = CREDIT_CARD_NAME,
                    issuerName = BANK_NAME,
                    currentStatementValue = BILL_VALUE,
                    dueDay = DUE_DAY
                )
            )
        )
        every { homeRepository.getLastMonthDifference(any()) } returns flowOf(0.0)
        every { homeRepository.getMonthlySpent(any()) } returns flowOf(0.0)
        viewModel = CreditCardBillsCardViewModel(homeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `doOnInit should collect credit cards from repository and update state`() {
        viewModel.doOnInit()

        TestCase.assertEquals(formatDouble(BILL_VALUE), viewModel.uiState.value.totalBill)
        TestCase.assertEquals(CREDIT_CARD_NAME, viewModel.uiState.value.bills.first().name)
        TestCase.assertEquals(formatDouble(BILL_VALUE), viewModel.uiState.value.bills.first().value)
        TestCase.assertEquals(BANK_NAME, viewModel.uiState.value.bills.first().bankName)
        TestCase.assertEquals(DUE_DAY, viewModel.uiState.value.bills.first().dueDay)
    }

    @Test
    fun `OnNewCreditCardBillValueChange with empty value should assign correctly`() {
        viewModel.onIntent(CreditCardBillsCardContract.Intent.OnNewCreditCardBillValueChange(EMPTY_STRING))

        TestCase.assertEquals(false, viewModel.uiState.value.isNewCreditCardBillValid)
        TestCase.assertEquals(EMPTY_STRING, viewModel.uiState.value.newCreditCardBill)
    }

    @Test
    fun `OnNewCreditCardBillValueChange with value should assign correctly`() {
        viewModel.onIntent(CreditCardBillsCardContract.Intent.OnNewCreditCardBillValueChange(VALID_BILL_STRING))

        TestCase.assertEquals(true, viewModel.uiState.value.isNewCreditCardBillValid)
        TestCase.assertEquals(VALID_BILL_STRING, viewModel.uiState.value.newCreditCardBill)
    }

    @Test
    fun `onNewCreditCardNameChange with empty value should assign correctly`() {
        viewModel.onIntent(CreditCardBillsCardContract.Intent.OnNewCreditCardNameChange(EMPTY_STRING))

        TestCase.assertEquals(false, viewModel.uiState.value.isNewCreditCardNameValid)
        TestCase.assertEquals(EMPTY_STRING, viewModel.uiState.value.newCreditCardName)
    }

    @Test
    fun `onNewCreditCardNameChange with value should assign correctly`() {
        viewModel.onIntent(CreditCardBillsCardContract.Intent.OnNewCreditCardNameChange(VALID_STRING))

        TestCase.assertEquals(true, viewModel.uiState.value.isNewCreditCardNameValid)
        TestCase.assertEquals(VALID_STRING, viewModel.uiState.value.newCreditCardName)
    }

    @Test
    fun `onBankChange with empty value should assign correctly`() {
        viewModel.onIntent(CreditCardBillsCardContract.Intent.OnBankChange(VALID_ID_STRING, EMPTY_STRING))

        TestCase.assertEquals(false, viewModel.uiState.value.isNewCreditCardBankNameValid)
        TestCase.assertEquals(EMPTY_STRING, viewModel.uiState.value.newCreditCardBankName)
        TestCase.assertEquals(VALID_ID_STRING, viewModel.uiState.value.newCreditCardBankIcon)
    }

    @Test
    fun `onBankChange with value should assign correctly`() {
        viewModel.onIntent(CreditCardBillsCardContract.Intent.OnBankChange(VALID_ID_STRING, VALID_BILL_STRING))

        TestCase.assertEquals(true, viewModel.uiState.value.isNewCreditCardBankNameValid)
        TestCase.assertEquals(VALID_BILL_STRING, viewModel.uiState.value.newCreditCardBankName)
        TestCase.assertEquals(VALID_ID_STRING, viewModel.uiState.value.newCreditCardBankIcon)
    }

    @Test
    fun `onNewCreditCardBillDueDayChange with zero value should assign correctly`() {
        viewModel.onIntent(CreditCardBillsCardContract.Intent.OnNewCreditCardBillDueDayChange(INVALID_DUE_DAY))

        TestCase.assertEquals(false, viewModel.uiState.value.isNewCreditCardBillDueDayValid)
        TestCase.assertEquals(INVALID_DUE_DAY, viewModel.uiState.value.newCreditCardBillDueDay)
    }

    @Test
    fun `onNewCreditCardBillDueDayChange with value should assign correctly`() {
        viewModel.onIntent(CreditCardBillsCardContract.Intent.OnNewCreditCardBillDueDayChange(VALID_DUE_DAY))

        TestCase.assertEquals(true, viewModel.uiState.value.isNewCreditCardBillDueDayValid)
        TestCase.assertEquals(VALID_DUE_DAY, viewModel.uiState.value.newCreditCardBillDueDay)
    }

    @Test
    fun `cleanNewAccount should assign default values`() {
        viewModel.onIntent(CreditCardBillsCardContract.Intent.CleanInputs)

        assertCleanedInputs()
    }

    @Test
    fun `onSaveClick with valid input should assign false to dialog`() {
        assignValidInputs()
        val mock = mutableStateOf(true)

        viewModel.onIntent(CreditCardBillsCardContract.Intent.OnSaveClick(mock))

        TestCase.assertEquals(false, mock.value)
    }

    @Test
    fun `onSaveClick with valid input should add new card`() {
        assignValidInputs()
        val mock = mutableStateOf(true)
        val newCard = CreditCardBillsCardContract.CreditCardBillUiState(
            name = VALID_STRING,
            value = VALID_BILL_STRING,
            bankName = VALID_STRING,
        )
        val newCardsList = viewModel.uiState.value.bills + newCard

        viewModel.onIntent(CreditCardBillsCardContract.Intent.OnSaveClick(mock))

        TestCase.assertEquals(newCardsList, viewModel.uiState.value.bills)
    }

    @Test
    fun `onSaveClick with valid input should clean inputs`() {
        assignValidInputs()
        val mock = mutableStateOf(true)

        viewModel.onIntent(CreditCardBillsCardContract.Intent.OnSaveClick(mock))

        assertCleanedInputs()
    }

    @Test
    fun `setDateFilter should assign value correctly`() {
        viewModel.onIntent(CreditCardBillsCardContract.Intent.OnDateFilterChange(VALID_DATE))

        TestCase.assertEquals(VALID_DATE, viewModel.uiState.value.currentDateFilter)
    }

    private fun assertCleanedInputs() {
        TestCase.assertEquals(EMPTY_STRING, viewModel.uiState.value.newCreditCardName)
        TestCase.assertEquals(EMPTY_STRING, viewModel.uiState.value.newCreditCardBill)
        TestCase.assertEquals(EMPTY_STRING, viewModel.uiState.value.newCreditCardBankName)
        TestCase.assertEquals(R.drawable.bank_icon, viewModel.uiState.value.newCreditCardBankIcon)
        TestCase.assertEquals(0, viewModel.uiState.value.newCreditCardBillDueDay)
        TestCase.assertEquals(true, viewModel.uiState.value.isNewCreditCardBankNameValid)
        TestCase.assertEquals(true, viewModel.uiState.value.isNewCreditCardNameValid)
        TestCase.assertEquals(true, viewModel.uiState.value.isNewCreditCardBillValid)
        TestCase.assertEquals(true, viewModel.uiState.value.isNewCreditCardBillDueDayValid)
    }

    private fun assignValidInputs() {
        viewModel.run {
            onIntent(CreditCardBillsCardContract.Intent.OnNewCreditCardNameChange(VALID_STRING))
            onIntent(CreditCardBillsCardContract.Intent.OnNewCreditCardBillValueChange(VALID_BILL_STRING))
            onIntent(CreditCardBillsCardContract.Intent.OnBankChange(R.drawable.bank_icon, VALID_STRING))
            onIntent(CreditCardBillsCardContract.Intent.OnNewCreditCardBillDueDayChange(VALID_DUE_DAY))
        }
    }

    private companion object {
        const val VALID_BILL_STRING = "123"
        const val EMPTY_STRING = ""
        const val VALID_STRING = "test"
        const val VALID_ID_STRING = 1
        const val VALID_DUE_DAY = 1
        const val INVALID_DUE_DAY = 0
        const val CREDIT_CARD_NAME = "Cartao"
        const val BANK_NAME = "Meu Banco"
        const val BILL_VALUE = 1000.0
        const val DUE_DAY = 30
        val VALID_DATE: LocalDate = LocalDate.of(1998, 10, 20)
    }
}
