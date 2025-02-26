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
import java.time.LocalDate

class CreditCardBillsCardViewModel : CreditCardBillsCardContract.CreditCardsBillCardViewModel() {
    override val uiState = MutableStateFlow(CreditCardsBillCardUiState())
    override var newCreditCardName: String by mutableStateOf("")
    override var isNewCreditCardNameValid: Boolean by mutableStateOf(true)
    override var newCreditCardBill: String by mutableStateOf("")
    override var isNewCreditCardBillValid: Boolean by mutableStateOf(true)
    override var newCreditCardBillDueDay: Int by mutableIntStateOf(0)
    override var isNewCreditCardBillDueDayValid: Boolean by mutableStateOf(true)
    override var newCreditCardBankName: String by mutableStateOf("")
    override var isNewCreditCardBankNameValid: Boolean by mutableStateOf(true)
    override var newCreditCardBankIcon: Int by mutableIntStateOf(R.drawable.bank_icon)
    override var currentDateFilter: LocalDate by mutableStateOf(LocalDate.now())

    override fun doOnInit(): CreditCardBillsCardViewModel {
        val bills = listOf(
            CreditCardBillUiState(
                name = CREDIT_CARD_MOCK,
                value = formatDouble(BILL_VALUE_MOCK),
                dueDay = DUE_DAY_MOCK,
                bankName = BANK_NAME_MOCK,
            ),
        )
        uiState.value = CreditCardsBillCardUiState(totalBill = formatDouble(TOTAL_BILL_MOCK), bills = bills)

        return this
    }

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

    override fun setDateFilter(date: LocalDate) {
        currentDateFilter = date
    }

    companion object {
        const val INVALID_BILL_DATE = 0
        const val CREDIT_CARD_MOCK = "Cartao"
        const val BILL_VALUE_MOCK = 1000.00
        const val DUE_DAY_MOCK = 30
        const val BANK_NAME_MOCK = "Meu Banco"
        const val TOTAL_BILL_MOCK = 2000.00
    }
}