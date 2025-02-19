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
import br.com.rbrthmn.ui.financialcompanion.utils.formatDouble
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class CreditCardBillsCardViewModel : CreditCardBillsCardContract.CreditCardsBillCardViewModel() {
    override val uiState = MutableStateFlow(CreditCardsBillCardUiState())

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

    override fun onNewCreditCardNameChange(name: String) = uiState.update {
        it.copy(
            newCreditCardName = name,
            isNewCreditCardNameValid = name.isNotBlank()
        )
    }

    override fun onNewCreditCardBillChange(bill: String) = uiState.update {
        it.copy(
            newCreditCardBill = bill,
            isNewCreditCardBillValid = bill.isNotBlank()
        )
    }

    override fun onBankChange(bankIcon: Int, bankName: String) = uiState.update {
        it.copy(
            newCreditCardBankIcon = bankIcon,
            newCreditCardBankName = bankName,
            isNewCreditCardBankNameValid = bankName.isNotBlank()
        )
    }

    override fun onNewCreditCardBillDueDayChange(day: Int) = uiState.update {
        it.copy(
            newCreditCardBillDueDay = day,
            isNewCreditCardBillDueDayValid = day != INVALID_BILL_DATE
        )
    }

    override fun onSaveClick(showDialog: MutableState<Boolean>) {
        if (validateInputs()) {
            showDialog.value = false
            val newCreditCard = CreditCardBillUiState(
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

    override fun cleanInputs() {
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

    override fun setDateFilter(date: LocalDate) = uiState.update {
        it.copy(currentDateFilter = date)
    }

    private companion object {
        const val INVALID_BILL_DATE = 0
    }
}