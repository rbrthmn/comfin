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
import androidx.lifecycle.ViewModel
import br.com.rbrthmn.ui.financialcompanion.uistates.BalanceCardUiState
import br.com.rbrthmn.ui.financialcompanion.uistates.FinancialOverviewUiState
import br.com.rbrthmn.ui.financialcompanion.utils.formatDouble
import br.com.rbrthmn.ui.financialcompanion.utils.formatString
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BalanceCardViewModel : ViewModel() {
    abstract val uiState: MutableStateFlow<BalanceCardUiState>
    abstract val newAccountBalance: String
    abstract val isNewAccountBalanceValid: Boolean
    abstract val newAccountDescription: String
    abstract val isNewAccountDescriptionValid: Boolean
    abstract val newAccountBank: String
    abstract val isNewAccountBankValid: Boolean
    abstract fun onInitialBalanceChange(balance: String)
    abstract fun onDescriptionChange(description: String)
    abstract fun onBankChange(bankId: Int, bankName: String)
    abstract fun onSaveClick(showDialog: MutableState<Boolean>)
    abstract fun cleanNewAccount()
}

class BalanceCardViewModelImpl : BalanceCardViewModel() {
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
    private var newAccountIcon by mutableIntStateOf(-1)
    override var isNewAccountBankValid by mutableStateOf(true)
        private set

    init {
        val accounts = listOf(
            FinancialOverviewUiState("Banco A", formatDouble(5000.00), "R$"),
            FinancialOverviewUiState("Banco B", formatDouble(10000.00), "R$"),
            FinancialOverviewUiState("Banco C", formatDouble(5.00), "R$")
        )
        uiState.value = BalanceCardUiState(
            totalBalance = formatDouble(100000.00),
            accounts = accounts,
        )
    }

    override fun onInitialBalanceChange(balance: String) {
        if (validateBalance(balance)) {
            isNewAccountBalanceValid = balance.isNotEmpty()
            newAccountBalance = balance
        }
    }

    private fun validateBalance(balance: String): Boolean {
        return try {
            formatString(balance)
            true
        } catch (e: NumberFormatException) {
            println("Invalid number format: $e")
            false
        }
    }

    override fun onDescriptionChange(description: String) {
        isNewAccountDescriptionValid = description.isNotEmpty()
        newAccountDescription = description
    }

    override fun onBankChange(bankId: Int, bankName: String) {
        isNewAccountBankValid = bankName.isNotEmpty()
        newAccountBank = bankName
        newAccountIcon = bankId
    }

    private fun validateInputs(): Boolean {
        isNewAccountBalanceValid = newAccountBalance.isNotEmpty()
        isNewAccountDescriptionValid = newAccountDescription.isNotEmpty()
        isNewAccountBankValid = newAccountBank.isNotEmpty()

        return validateBalance(newAccountBalance) && isNewAccountBalanceValid && isNewAccountDescriptionValid && isNewAccountBankValid
    }

    override fun onSaveClick(showDialog: MutableState<Boolean>) {
        if (validateInputs()) {
            showDialog.value = false
            val newAccount = FinancialOverviewUiState(
                name = newAccountDescription,
                value = formatString(newAccountBalance),
                financialInstitutionName = newAccountBank,
                balanceCurrency = "R$"
            )
            uiState.value = uiState.value.copy(accounts = uiState.value.accounts + newAccount)
            cleanNewAccount()
        }
    }

    override fun cleanNewAccount() {
        newAccountBalance = ""
        newAccountDescription = ""
        newAccountBank = ""
        isNewAccountBalanceValid = true
        isNewAccountDescriptionValid = true
        isNewAccountBankValid = true
    }
}
