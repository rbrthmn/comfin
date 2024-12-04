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

package br.com.rbrthmn.ui.financialcompanion.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.com.rbrthmn.R
import br.com.rbrthmn.contracts.BalanceContract
import br.com.rbrthmn.ui.financialcompanion.uistates.BalanceCardUiState
import br.com.rbrthmn.ui.financialcompanion.uistates.FinancialOverviewUiState
import br.com.rbrthmn.ui.financialcompanion.utils.formatDouble
import br.com.rbrthmn.ui.financialcompanion.utils.formatString
import kotlinx.coroutines.flow.MutableStateFlow

class BalanceCardViewModel : BalanceContract.BalanceCardViewModel() {
    override var uiState = MutableStateFlow(BalanceCardUiState())
        private set

    override var newAccountBalance by mutableStateOf("")
        private set

    override var isNewAccountBalanceValid by mutableStateOf(true)
        private set

    override var newAccountDescription by mutableStateOf("")
        private set

    override var isNewAccountDescriptionValid by mutableStateOf(true)
        private set

    override var newAccountBank by mutableStateOf("")
        private set

    override var newAccountBankIcon by mutableIntStateOf(R.drawable.bank_icon)
        private set

    override var isNewAccountBankValid by mutableStateOf(true)
        private set

    init {
        val accounts = listOf(
            FinancialOverviewUiState("Banco A", formatDouble(5000.00), "R$"),
            FinancialOverviewUiState("Banco B", formatDouble(10000.00), "R$"),
            FinancialOverviewUiState("Banco C", formatDouble(5.00), "R$")
        )
        val totalBalance = 100000.00
        uiState.value = BalanceCardUiState(
            totalBalance = formatDouble(totalBalance),
            accounts = accounts,
        )
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
            val newAccount = FinancialOverviewUiState(
                name = newAccountDescription,
                value = formatString(newAccountBalance),
                financialInstitutionName = newAccountBank,
            )
            uiState.value = uiState.value.copy(accounts = uiState.value.accounts + newAccount)
            cleanNewAccount()
        }
    }

    override fun cleanNewAccount() {
        newAccountBalance = ""
        newAccountDescription = ""
        newAccountBank = ""
        newAccountBankIcon = -1
        isNewAccountBalanceValid = true
        isNewAccountDescriptionValid = true
        isNewAccountBankValid = true
    }
}
