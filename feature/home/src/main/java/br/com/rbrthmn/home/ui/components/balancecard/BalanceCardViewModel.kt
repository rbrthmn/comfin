/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Modifications made by Roberto Kenzo Hamano, 2024
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.rbrthmn.home.ui.components.balancecard

import androidx.compose.runtime.MutableState
import br.com.rbrthmn.ui.R
import br.com.rbrthmn.ui.utils.formatDouble
import br.com.rbrthmn.ui.utils.formatString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class BalanceCardViewModel : BalanceCardContract.ViewModel() {
    override val uiState = MutableStateFlow(BalanceCardContract.BalanceCardUiState())

    override fun doOnInit(): BalanceCardViewModel {
        val accounts = listOf(
            BalanceCardContract.BankAccountBalanceUiState(
                name = ACCOUNT_NAME_MOCK,
                value = formatDouble(ACCOUNT_VALUE_MOCK),
                bankName = BANK_NAME_MOCK,
            )
        )
        uiState.value = BalanceCardContract.BalanceCardUiState(
            totalBalance = formatDouble(TOTAL_BALANCE_MOCK),
            accounts = accounts,
        )

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

    private fun setDateFilter(date: LocalDate) = uiState.update {
        it.copy(currentDateFilter = date)
    }

    companion object {
        const val ACCOUNT_NAME_MOCK = "Conta"
        const val ACCOUNT_VALUE_MOCK = 1000.00
        const val BANK_NAME_MOCK = "Meu Banco"
        const val TOTAL_BALANCE_MOCK = 2000.00
    }
}
