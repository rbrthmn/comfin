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

package br.com.rbrthmn.ui.financialcompanion.screens.home.components.creditcardbillscard

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.utils.formatDouble
import kotlinx.coroutines.flow.MutableStateFlow

class CreditCardBillsCardViewModel : CreditCardBillsCardContract.CreditCardsBillCardViewModel() {
    override var uiState = MutableStateFlow(CreditCardsBillCardUiState())
        private set

    override var newCreditCardName: String by mutableStateOf("")
        private set

    override var isNewCreditCardNameValid: Boolean by mutableStateOf(true)
        private set

    override var newCreditCardBill: String by mutableStateOf("")
        private set

    override var isNewCreditCardBillValid: Boolean by mutableStateOf(true)
        private set

    override var newCreditCardBillDueDay: Int by mutableIntStateOf(0)
        private set

    override var isNewCreditCardBillDueDayValid: Boolean by mutableStateOf(true)
        private set

    override var newCreditCardBankName: String by mutableStateOf("")
        private set

    override var isNewCreditCardBankNameValid: Boolean by mutableStateOf(true)
        private set

    override var newCreditCardBankIcon: Int by mutableIntStateOf(R.drawable.bank_icon)
        private set

    override fun onNewCreditCardNameChange(name: String) {
        isNewCreditCardNameValid = name.isNotBlank()
        newCreditCardName = name
    }

    override fun onNewCreditCardBillChange(bill: String) {
        isNewCreditCardBillValid = bill.isNotBlank()
        newCreditCardBill = bill
    }

    override fun onBankChange(bankIcon: Int, bankName: String) {
        isNewCreditCardBankNameValid = bankName.isNotBlank()
        newCreditCardBankIcon = bankIcon
        newCreditCardBankName = bankName
    }

    override fun onNewCreditCardBillDueDayChange(day: Int) {
        isNewCreditCardBillDueDayValid = day != INVALID_BILL_DATE
        newCreditCardBillDueDay = day
    }

    override fun onSaveClick(showDialog: MutableState<Boolean>) {
        if (validateInputs()) {
            showDialog.value = false
            val newCreditCard = CreditCardBillUiState(
                name = newCreditCardName,
                value = newCreditCardBill,
                bankName = newCreditCardBankName,
                bankIcon = newCreditCardBankIcon
            )
            uiState.value = uiState.value.copy(bills = uiState.value.bills + newCreditCard)
            cleanInputs()
        }
    }

    private fun validateInputs(): Boolean {
        isNewCreditCardNameValid = newCreditCardName.isNotBlank()
        isNewCreditCardBillValid = newCreditCardBill.isNotBlank()
        isNewCreditCardBillDueDayValid = newCreditCardBillDueDay != INVALID_BILL_DATE
        isNewCreditCardBankNameValid = newCreditCardBankName.isNotBlank()

        return isNewCreditCardNameValid && isNewCreditCardBillValid && isNewCreditCardBillDueDayValid && isNewCreditCardBankNameValid
    }

    override fun cleanInputs() {
        newCreditCardName = ""
        newCreditCardBill = ""
        newCreditCardBillDueDay = INVALID_BILL_DATE
        newCreditCardBankName = ""
        newCreditCardBankIcon = R.drawable.bank_icon
        isNewCreditCardNameValid = true
        isNewCreditCardBillValid = true
        isNewCreditCardBillDueDayValid = true
        isNewCreditCardBankNameValid = true
    }

    init {
        val bills = listOf(
            CreditCardBillUiState("Cartao A", formatDouble(5000.00), "R$"),
            CreditCardBillUiState("Cartao B", formatDouble(10000.00), "R$"),
            CreditCardBillUiState("Cartao C", formatDouble(5.00), "R$")
        )
        val totalBill = 1200.00

        uiState.value =
            CreditCardsBillCardUiState(totalBill = formatDouble(totalBill), bills = bills)
    }

    private companion object {
        const val INVALID_BILL_DATE = 0
    }
}