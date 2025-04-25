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

import br.com.rbrthmn.R
import br.com.rbrthmn.contract.BaseContract
import java.time.LocalDate

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
) : BaseContract.BaseUiState
