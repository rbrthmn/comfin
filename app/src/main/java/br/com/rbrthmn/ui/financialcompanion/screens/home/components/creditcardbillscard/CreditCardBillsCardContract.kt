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
import br.com.rbrthmn.ui.financialcompanion.BaseViewModel
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

interface CreditCardBillsCardContract {
    abstract class ViewModel : BaseViewModel<CreditCardsBillCardUiState, Intent>() {
        abstract override val uiState: StateFlow<CreditCardsBillCardUiState>
        abstract override fun doOnInit(): CreditCardBillsCardViewModel
    }

    sealed class Intent {
        data class OnNewCreditCardNameChange(val name: String) : Intent()
        data class OnNewCreditCardBillValueChange(val bill: String) : Intent()
        data class OnBankChange(val bankIcon: Int, val bankName: String) : Intent()
        data class OnNewCreditCardBillDueDayChange(val day: Int) : Intent()
        data class OnSaveClick(val showDialog: MutableState<Boolean>) : Intent()
        data object CleanInputs : Intent()
        data class OnDateFilterChange(val date: LocalDate) : Intent()
    }

    data class CreditCardBillUiState(
        val name: String = "",
        val value: String = "",
        val bankName: String = "",
        val bankIcon: Int = R.drawable.bank_icon,
        val canValueBeEdited: Boolean = false,
        val dueDay: Int = 0
    )

    data class CreditCardsBillCardUiState(
        val totalBill: String = "",
        val bills: List<CreditCardBillUiState> = listOf(),
        val newCreditCardName: String = "",
        val isNewCreditCardNameValid: Boolean = true,
        val newCreditCardBill: String = "",
        val isNewCreditCardBillValid: Boolean = true,
        val newCreditCardBillDueDay: Int = 0,
        val isNewCreditCardBillDueDayValid: Boolean = true,
        val newCreditCardBankName: String = "",
        val isNewCreditCardBankNameValid: Boolean = true,
        val newCreditCardBankIcon: Int = R.drawable.bank_icon,
        var currentDateFilter: LocalDate = LocalDate.now()
    )
}
