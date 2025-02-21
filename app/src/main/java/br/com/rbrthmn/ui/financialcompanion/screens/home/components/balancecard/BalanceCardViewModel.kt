/*
 *
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
 *
 */

package br.com.rbrthmn.ui.financialcompanion.screens.home.components.balancecard

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.utils.formatDouble
import br.com.rbrthmn.ui.financialcompanion.utils.formatString
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

class BalanceCardViewModel : BalanceCardContract.BalanceCardViewModel() {
    override var uiState = MutableStateFlow(BalanceCardUiState())
    override var newAccountBalance by mutableStateOf("")
    override var isNewAccountBalanceValid by mutableStateOf(true)
    override var newAccountDescription by mutableStateOf("")
    override var isNewAccountDescriptionValid by mutableStateOf(true)
    override var newAccountBank by mutableStateOf("")
    override var newAccountBankIcon by mutableIntStateOf(R.drawable.bank_icon)
    override var isNewAccountBankValid by mutableStateOf(true)
    override var currentDateFilter: LocalDate by mutableStateOf(LocalDate.now())

    override fun doOnInit(): BalanceCardViewModel {
        val accounts = listOf(
            BankAccountBalanceUiState(
                name = ACCOUNT_NAME_MOCK,
                value = formatDouble(ACCOUNT_VALUE_MOCK),
                bankName = BANK_NAME_MOCK,
            )
        )
        uiState.value = BalanceCardUiState(
            totalBalance = formatDouble(TOTAL_BALANCE_MOCK),
            accounts = accounts,
        )

        return this
    }

    override fun onInitialBalanceChange(balance: String) {
        isNewAccountBalanceValid = balance.isNotBlank()
        newAccountBalance = balance
    }

    override fun onDescriptionChange(description: String) {
        isNewAccountDescriptionValid = description.isNotBlank()
        newAccountDescription = description
    }

    override fun onBankChange(bankId: Int, bankName: String) {
        isNewAccountBankValid = bankName.isNotBlank()
        newAccountBank = bankName
        newAccountBankIcon = bankId
    }

    private fun validateInputs(): Boolean {
        isNewAccountBalanceValid = newAccountBalance.isNotBlank()
        isNewAccountDescriptionValid = newAccountDescription.isNotBlank()
        isNewAccountBankValid = newAccountBank.isNotBlank()

        return isNewAccountBalanceValid && isNewAccountDescriptionValid && isNewAccountBankValid
    }

    override fun onSaveClick(showDialog: MutableState<Boolean>) {
        if (validateInputs()) {
            showDialog.value = false
            val newAccount = BankAccountBalanceUiState(
                name = newAccountDescription,
                value = formatString(newAccountBalance),
                bankName = newAccountBank,
            )
            uiState.value = uiState.value.copy(accounts = uiState.value.accounts + newAccount)
            cleanNewAccount()
        }
    }

    override fun cleanNewAccount() {
        newAccountBalance = ""
        newAccountDescription = ""
        newAccountBank = ""
        newAccountBankIcon = R.drawable.bank_icon
        isNewAccountBalanceValid = true
        isNewAccountDescriptionValid = true
        isNewAccountBankValid = true
    }

    override fun setDateFilter(date: LocalDate) {
        currentDateFilter = date
    }

    companion object {
        const val ACCOUNT_NAME_MOCK = "Conta"
        const val ACCOUNT_VALUE_MOCK = 1000.00
        const val BANK_NAME_MOCK = "Meu Banco"
        const val TOTAL_BALANCE_MOCK = 2000.00
    }
}
