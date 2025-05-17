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
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.creditcardbillscard.CreditCardBillsCardContract.Intent
import br.com.rbrthmn.ui.financialcompanion.utils.formatDouble
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.creditcardbillscard.CreditCardBillsCardContract as Contract

class CreditCardBillsCardViewModel : Contract.ViewModel() {
    override val uiState = MutableStateFlow(Contract.CreditCardsBillCardUiState())

    override fun doOnInit(): CreditCardBillsCardViewModel {
        val bills = listOf(
            Contract.CreditCardBillUiState(
                name = CREDIT_CARD_MOCK,
                value = formatDouble(BILL_VALUE_MOCK),
                dueDay = DUE_DAY_MOCK,
                bankName = BANK_NAME_MOCK,
            ),
        )
        uiState.value = Contract.CreditCardsBillCardUiState(
            totalBill = formatDouble(TOTAL_BILL_MOCK),
            bills = bills
        )

        return this
    }

    override fun onIntent(intent: Intent) {
        when (intent) {
            Intent.CleanInputs -> cleanInputs()
            is Intent.OnBankChange -> onBankChange(intent.bankIcon, intent.bankName)
            is Intent.OnDateFilterChange -> onDateFilterChange(intent.date)
            is Intent.OnNewCreditCardBillValueChange -> onNewCreditCardBillChange(intent.bill)
            is Intent.OnNewCreditCardBillDueDayChange -> onNewCreditCardBillDueDayChange(
                intent.day
            )

            is Intent.OnNewCreditCardNameChange -> onNewCreditCardNameChange(intent.name)
            is Intent.OnSaveClick -> onSaveClick(intent.showDialog)
        }
    }

    private fun onNewCreditCardNameChange(name: String) = uiState.update {
        it.copy(
            newCreditCardName = name,
            isNewCreditCardNameValid = name.isNotBlank()
        )
    }

    private fun onNewCreditCardBillChange(bill: String) = uiState.update {
        it.copy(
            newCreditCardBill = bill,
            isNewCreditCardBillValid = bill.isNotBlank()
        )
    }

    private fun onBankChange(bankIcon: Int, bankName: String) = uiState.update {
        it.copy(
            newCreditCardBankIcon = bankIcon,
            newCreditCardBankName = bankName,
            isNewCreditCardBankNameValid = bankName.isNotBlank()
        )
    }

    private fun onNewCreditCardBillDueDayChange(day: Int) = uiState.update {
        it.copy(
            newCreditCardBillDueDay = day,
            isNewCreditCardBillDueDayValid = day != INVALID_BILL_DATE
        )
    }

    private fun onSaveClick(showDialog: MutableState<Boolean>) {
        if (validateInputs()) {
            showDialog.value = false
            val newCreditCard = Contract.CreditCardBillUiState(
                name = uiState.value.newCreditCardName,
                value = uiState.value.newCreditCardBill,
                bankName = uiState.value.newCreditCardBankName,
                bankIcon = uiState.value.newCreditCardBankIcon
            )
            uiState.value = uiState.value.copy(bills = uiState.value.bills + newCreditCard)
            cleanInputs()
        }
    }

    private fun validateInputs(): Boolean {
        uiState.update {
            it.copy(
                isNewCreditCardNameValid = uiState.value.newCreditCardName.isNotBlank(),
                isNewCreditCardBillValid = uiState.value.newCreditCardBill.isNotBlank(),
                isNewCreditCardBillDueDayValid = uiState.value.newCreditCardBillDueDay != INVALID_BILL_DATE,
                isNewCreditCardBankNameValid = uiState.value.newCreditCardBankName.isNotBlank()
            )
        }

        return with(uiState.value) {
            isNewCreditCardNameValid && isNewCreditCardBillValid && isNewCreditCardBillDueDayValid && isNewCreditCardBankNameValid
        }
    }

    private fun cleanInputs() {
        uiState.update {
            it.copy(
                newCreditCardName = "",
                newCreditCardBill = "",
                newCreditCardBillDueDay = INVALID_BILL_DATE,
                newCreditCardBankName = "",
                newCreditCardBankIcon = R.drawable.bank_icon,
                isNewCreditCardNameValid = true,
                isNewCreditCardBillValid = true,
                isNewCreditCardBillDueDayValid = true,
                isNewCreditCardBankNameValid = true
            )
        }
    }

    private fun onDateFilterChange(date: LocalDate) = uiState.update {
        it.copy(currentDateFilter = date)
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