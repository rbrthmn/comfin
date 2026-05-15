package br.com.rbrthmn.home.ui.components.balancecard

import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import br.com.rbrthmn.data.home.repository.HomeRepository
import br.com.rbrthmn.ui.R
import br.com.rbrthmn.ui.utils.formatDouble
import br.com.rbrthmn.ui.utils.formatString
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class BalanceCardViewModel(private val homeRepository: HomeRepository) : BalanceCardContract.ViewModel() {
    override val uiState = MutableStateFlow(BalanceCardContract.BalanceCardUiState())
    private val dateFilter = MutableStateFlow(LocalDate.now())

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun doOnInit(): BalanceCardViewModel {
        viewModelScope.launch {
            dateFilter.flatMapLatest { date ->
                homeRepository.getAccountsSummary(date)
            }.collect { summary ->
                uiState.update {
                    it.copy(
                        totalBalance = formatDouble(summary.totalBalance),
                        accounts = summary.accounts.map { account ->
                            BalanceCardContract.BankAccountBalanceUiState(
                                name = account.name,
                                value = formatDouble(account.balance),
                                bankName = account.bankName
                            )
                        }
                    )
                }
            }
        }
        return this
    }

    override fun onIntent(intent: BalanceCardContract.Intent) {
        when (intent) {
            BalanceCardContract.Intent.CleanNewAccount -> cleanNewAccount()
            is BalanceCardContract.Intent.OnBankChange -> onBankChange(
                intent.bankId,
                intent.bankName
            )

            is BalanceCardContract.Intent.OnDateFilterChange -> setDateFilter(
                intent.date
            )

            is BalanceCardContract.Intent.OnDescriptionChange -> onDescriptionChange(
                intent.description
            )

            is BalanceCardContract.Intent.OnInitialBalanceChange -> onInitialBalanceChange(
                intent.balance
            )

            is BalanceCardContract.Intent.OnSaveClick -> onSaveClick(intent.showDialog)
        }
    }

    private fun onInitialBalanceChange(balance: String) = uiState.update {
        it.copy(
            isNewAccountBalanceValid = balance.isNotBlank(),
            newAccountBalance = balance
        )
    }

    private fun onDescriptionChange(description: String) = uiState.update {
        it.copy(
            isNewAccountDescriptionValid = description.isNotBlank(),
            newAccountDescription = description
        )
    }


    private fun onBankChange(bankId: Int, bankName: String) = uiState.update {
        it.copy(
            isNewAccountBankValid = bankName.isNotBlank(),
            newAccountBank = bankName,
            newAccountBankIcon = bankId
        )
    }


    private fun validateInputs(): Boolean {
        uiState.update {
            it.copy(
                isNewAccountBalanceValid = it.newAccountBalance.isNotBlank(),
                isNewAccountDescriptionValid = it.newAccountDescription.isNotBlank(),
                isNewAccountBankValid = it.newAccountBank.isNotBlank()
            )
        }

        return with(uiState.value) {
            isNewAccountBalanceValid && isNewAccountDescriptionValid && isNewAccountBankValid
        }
    }

    private fun onSaveClick(showDialog: MutableState<Boolean>) {
        if (validateInputs()) {
            showDialog.value = false
            val newAccount = BalanceCardContract.BankAccountBalanceUiState(
                name = uiState.value.newAccountDescription,
                value = formatString(uiState.value.newAccountBalance),
                bankName = uiState.value.newAccountBank,
            )
            uiState.value = uiState.value.copy(accounts = uiState.value.accounts + newAccount)
            cleanNewAccount()
        }
    }

    private fun cleanNewAccount() =
        uiState.update {
            it.copy(
                newAccountBalance = "",
                newAccountDescription = "",
                newAccountBank = "",
                newAccountBankIcon = R.drawable.bank_icon,
                isNewAccountBalanceValid = true,
                isNewAccountDescriptionValid = true,
                isNewAccountBankValid = true
            )
        }

    private fun setDateFilter(date: LocalDate) {
        dateFilter.value = date
        uiState.update { it.copy(currentDateFilter = date) }
    }
}
