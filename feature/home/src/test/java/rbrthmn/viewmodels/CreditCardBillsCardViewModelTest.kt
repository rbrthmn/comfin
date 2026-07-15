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
    fun `doOnInit should display the total bill and per-card bills`() {
        viewModel.doOnInit()
        val expectedBill = CreditCardBillsCardContract.CreditCardBillUiState(
            name = CreditCardBillsCardViewModel.Companion.CREDIT_CARD_MOCK,
            value = formatDouble(CreditCardBillsCardViewModel.Companion.BILL_VALUE_MOCK),
            dueDay = CreditCardBillsCardViewModel.Companion.DUE_DAY_MOCK,
            bankName = CreditCardBillsCardViewModel.Companion.BANK_NAME_MOCK,
        )

        TestCase.assertEquals(
            formatDouble(CreditCardBillsCardViewModel.Companion.TOTAL_BILL_MOCK),
            viewModel.uiState.value.totalBill
        )
        TestCase.assertEquals(expectedBill.value, viewModel.uiState.value.bills.first().value)
        TestCase.assertEquals(expectedBill.name, viewModel.uiState.value.bills.first().name)
        TestCase.assertEquals(expectedBill.bankName, viewModel.uiState.value.bills.first().bankName)
        TestCase.assertEquals(expectedBill.dueDay, viewModel.uiState.value.bills.first().dueDay)
    }

    @Test
    fun `onSaveClick with valid input should add the new card and close the dialog`() {
        assignValidInputs()
        val showDialog = mutableStateOf(true)
        val newCard = CreditCardBillsCardContract.CreditCardBillUiState(
            name = VALID_STRING,
            value = VALID_BILL_STRING,
            bankName = VALID_STRING,
        )
        val expectedBills = viewModel.uiState.value.bills + newCard

        viewModel.onIntent(CreditCardBillsCardContract.Intent.OnSaveClick(showDialog))

        TestCase.assertEquals(expectedBills, viewModel.uiState.value.bills)
        TestCase.assertEquals(false, showDialog.value)
        assertCleanedInputs()
    }

    @Test
    fun `onNewCreditCardNameChange with empty value should flag card name as required`() {
        viewModel.onIntent(CreditCardBillsCardContract.Intent.OnNewCreditCardNameChange(EMPTY_STRING))

        TestCase.assertEquals(false, viewModel.uiState.value.isNewCreditCardNameValid)
    }

    @Test
    fun `onNewCreditCardNameChange with valid value should accept the card name`() {
        viewModel.onIntent(CreditCardBillsCardContract.Intent.OnNewCreditCardNameChange(VALID_STRING))

        TestCase.assertEquals(true, viewModel.uiState.value.isNewCreditCardNameValid)
    }

    @Test
    fun `onNewCreditCardBillValueChange with empty value should flag bill value as required`() {
        viewModel.onIntent(
            CreditCardBillsCardContract.Intent.OnNewCreditCardBillValueChange(
                EMPTY_STRING
            )
        )

        TestCase.assertEquals(false, viewModel.uiState.value.isNewCreditCardBillValid)
    }

    @Test
    fun `onNewCreditCardBillValueChange with valid value should accept the bill value`() {
        viewModel.onIntent(
            CreditCardBillsCardContract.Intent.OnNewCreditCardBillValueChange(
                VALID_BILL_STRING
            )
        )

        TestCase.assertEquals(true, viewModel.uiState.value.isNewCreditCardBillValid)
    }

    @Test
    fun `onBankChange with empty bank should flag bank as required`() {
        viewModel.onIntent(
            CreditCardBillsCardContract.Intent.OnBankChange(
                VALID_ID_STRING,
                EMPTY_STRING
            )
        )

        TestCase.assertEquals(false, viewModel.uiState.value.isNewCreditCardBankNameValid)
    }

    @Test
    fun `onBankChange with a bank selected should accept the bank`() {
        viewModel.onIntent(
            CreditCardBillsCardContract.Intent.OnBankChange(
                VALID_ID_STRING,
                VALID_STRING
            )
        )

        TestCase.assertEquals(true, viewModel.uiState.value.isNewCreditCardBankNameValid)
    }

    @Test
    fun `onNewCreditCardBillDueDayChange with zero value should flag due day as required`() {
        viewModel.onIntent(
            CreditCardBillsCardContract.Intent.OnNewCreditCardBillDueDayChange(
                INVALID_DUE_DAY
            )
        )

        TestCase.assertEquals(false, viewModel.uiState.value.isNewCreditCardBillDueDayValid)
    }

    @Test
    fun `onNewCreditCardBillDueDayChange with valid day should accept the due day`() {
        viewModel.onIntent(
            CreditCardBillsCardContract.Intent.OnNewCreditCardBillDueDayChange(
                VALID_DUE_DAY
            )
        )

        TestCase.assertEquals(true, viewModel.uiState.value.isNewCreditCardBillDueDayValid)
    }

    @Test
    fun `cleanInputs should discard the new card form`() {
        assignValidInputs()

        viewModel.onIntent(CreditCardBillsCardContract.Intent.CleanInputs)

        assertCleanedInputs()
    }

    @Test
    fun `onDateFilterChange should update the selected month`() {
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
