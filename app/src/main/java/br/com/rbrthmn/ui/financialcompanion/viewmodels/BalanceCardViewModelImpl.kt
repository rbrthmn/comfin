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
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.uistates.BalanceCardUiState
import br.com.rbrthmn.ui.financialcompanion.uistates.FinancialOverviewUiState
import br.com.rbrthmn.ui.financialcompanion.utils.formatDouble
import br.com.rbrthmn.ui.financialcompanion.utils.formatString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BalanceCardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BalanceCardUiState())
    val uiState: StateFlow<BalanceCardUiState> = _uiState.asStateFlow()

    var newAccountBalance by mutableStateOf("")
        private set
    var isNewAccountBalanceValid by mutableStateOf(true)

    var newAccountDescription by mutableStateOf("")
        private set
    var isNewAccountDescriptionValid by mutableStateOf(true)

    private var newAccountBank by mutableStateOf("")
    private var newAccountIcon by mutableIntStateOf(-1)
    var isNewAccountBankValid by mutableStateOf(true)

    init {
        val accounts = listOf(
            FinancialOverviewUiState("Banco A", formatDouble(5000.00), "R$"),
            FinancialOverviewUiState("Banco B", formatDouble(10000.00), "R$"),
            FinancialOverviewUiState("Banco C", formatDouble(5.00), "R$")
        )
        _uiState.value = BalanceCardUiState(
            totalBalance = formatDouble(100000.00),
            accounts = accounts,
        )
    }

    fun onInitialBalanceChange(balance: String) {
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

    fun onDescriptionChange(description: String) {
        isNewAccountDescriptionValid = description.isNotEmpty()
        newAccountDescription = description
    }

    fun onBankChange(bankId: Int, bankName: String) {
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

    fun onSaveClick(showDialog: MutableState<Boolean>) {
        if (validateInputs()) {
            showDialog.value = false
            val newAccount = FinancialOverviewUiState(
                name = newAccountDescription,
                value = formatString(newAccountBalance),
                financialInstitutionName = newAccountBank,
                balanceCurrency = "R$"
            )
            _uiState.value = _uiState.value.copy(accounts = _uiState.value.accounts + newAccount)
            cleanNewAccount()
        }
    }

    fun cleanNewAccount() {
        newAccountBalance = ""
        newAccountDescription = ""
        newAccountBank = ""
        isNewAccountBalanceValid = true
        isNewAccountDescriptionValid = true
        isNewAccountBankValid = true
    }
}

data class BalanceCardUiState(
    val totalBalance: String = "",
    val accounts: List<FinancialOverviewUiState> = listOf()
)

data class FinancialOverviewUiState(
    val name: String = "",
    val value: String = "",
    val balanceCurrency: String = "",
    val financialInstitutionName: String = "",
    val financialInstitutionIcon: Int = R.drawable.account_balance,
    val canValueBeEdited: Boolean = true
)
