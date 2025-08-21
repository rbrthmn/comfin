package rbrthmn.viewmodels

import androidx.compose.runtime.mutableStateOf
import br.com.rbrthmn.home.ui.components.creditcardbillscard.CreditCardBillsCardContract
import br.com.rbrthmn.home.ui.components.creditcardbillscard.CreditCardBillsCardViewModel
import br.com.rbrthmn.ui.R
import br.com.rbrthmn.ui.utils.formatDouble
import junit.framework.TestCase
import org.junit.Test
import java.time.LocalDate

class CreditCardBillsCardViewModelTest {
    private val viewModel =
        CreditCardBillsCardViewModel()

    @Test
    fun `doOnInit should assign initial values`() {
        viewModel.doOnInit()
        val expectedList = listOf(
            CreditCardBillsCardContract.CreditCardBillUiState(
                name = CreditCardBillsCardViewModel.Companion.CREDIT_CARD_MOCK,
                value = formatDouble(CreditCardBillsCardViewModel.Companion.BILL_VALUE_MOCK),
                dueDay = CreditCardBillsCardViewModel.Companion.DUE_DAY_MOCK,
                bankName = CreditCardBillsCardViewModel.Companion.BANK_NAME_MOCK,
            )
        )

        TestCase.assertEquals(
            formatDouble(CreditCardBillsCardViewModel.Companion.TOTAL_BILL_MOCK),
            viewModel.uiState.value.totalBill
        )
        TestCase.assertEquals(
            expectedList.first().value,
            viewModel.uiState.value.bills.first().value
        )
        TestCase.assertEquals(expectedList.first().name, viewModel.uiState.value.bills.first().name)
        TestCase.assertEquals(
            expectedList.first().bankName,
            viewModel.uiState.value.bills.first().bankName
        )
        TestCase.assertEquals(
            expectedList.first().dueDay,
            viewModel.uiState.value.bills.first().dueDay
        )
    }

    @Test
    fun `OnNewCreditCardBillValueChange with empty value should assign correctly`() {
        viewModel.onIntent(
            CreditCardBillsCardContract.Intent.OnNewCreditCardBillValueChange(
                EMPTY_STRING
            )
        )

        TestCase.assertEquals(false, viewModel.uiState.value.isNewCreditCardBillValid)
        TestCase.assertEquals(EMPTY_STRING, viewModel.uiState.value.newCreditCardBill)
    }

    @Test
    fun `OnNewCreditCardBillValueChange with value should assign correctly`() {
        viewModel.onIntent(
            CreditCardBillsCardContract.Intent.OnNewCreditCardBillValueChange(
                VALID_BILL_STRING
            )
        )

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
        viewModel.onIntent(
            CreditCardBillsCardContract.Intent.OnBankChange(
                VALID_ID_STRING,
                EMPTY_STRING
            )
        )

        TestCase.assertEquals(false, viewModel.uiState.value.isNewCreditCardBankNameValid)
        TestCase.assertEquals(EMPTY_STRING, viewModel.uiState.value.newCreditCardBankName)
        TestCase.assertEquals(VALID_ID_STRING, viewModel.uiState.value.newCreditCardBankIcon)
    }

    @Test
    fun `onBankChange with value should assign correctly`() {
        viewModel.onIntent(
            CreditCardBillsCardContract.Intent.OnBankChange(
                VALID_ID_STRING,
                VALID_BILL_STRING
            )
        )

        TestCase.assertEquals(true, viewModel.uiState.value.isNewCreditCardBankNameValid)
        TestCase.assertEquals(VALID_BILL_STRING, viewModel.uiState.value.newCreditCardBankName)
        TestCase.assertEquals(VALID_ID_STRING, viewModel.uiState.value.newCreditCardBankIcon)
    }

    @Test
    fun `onNewCreditCardBillDueDayChange with zero value should assign correctly`() {
        viewModel.onIntent(
            CreditCardBillsCardContract.Intent.OnNewCreditCardBillDueDayChange(
                INVALID_DUE_DAY
            )
        )

        TestCase.assertEquals(false, viewModel.uiState.value.isNewCreditCardBillDueDayValid)
        TestCase.assertEquals(INVALID_DUE_DAY, viewModel.uiState.value.newCreditCardBillDueDay)
    }

    @Test
    fun `onNewCreditCardBillDueDayChange with value should assign correctly`() {
        viewModel.onIntent(
            CreditCardBillsCardContract.Intent.OnNewCreditCardBillDueDayChange(
                VALID_DUE_DAY
            )
        )

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
    fun `onSaveClick with valid input should add new account`() {
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
            onIntent(
                CreditCardBillsCardContract.Intent.OnNewCreditCardBillValueChange(
                    VALID_BILL_STRING
                )
            )
            onIntent(
                CreditCardBillsCardContract.Intent.OnBankChange(
                    R.drawable.bank_icon,
                    VALID_STRING
                )
            )
            onIntent(
                CreditCardBillsCardContract.Intent.OnNewCreditCardBillDueDayChange(
                    VALID_DUE_DAY
                )
            )
        }
    }

    @Test
    fun `setDateFilter should assign value correctly`() {
        viewModel.onIntent(CreditCardBillsCardContract.Intent.OnDateFilterChange(VALID_DATE))

        TestCase.assertEquals(VALID_DATE, viewModel.uiState.value.currentDateFilter)
    }

    private companion object {
        const val VALID_BILL_STRING = "123"
        const val EMPTY_STRING = ""
        const val VALID_STRING = "test"
        const val VALID_ID_STRING = 1
        const val VALID_DUE_DAY = 1
        const val INVALID_DUE_DAY = 0
        val VALID_DATE: LocalDate = LocalDate.of(1998, 10, 20)
    }
}