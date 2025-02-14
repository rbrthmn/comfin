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
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

interface BalanceCardContract {
    abstract class BalanceCardViewModel : ViewModel() {
        abstract val uiState: MutableStateFlow<BalanceCardUiState>
        abstract val newAccountBalance: String
        abstract val isNewAccountBalanceValid: Boolean
        abstract val newAccountDescription: String
        abstract val isNewAccountDescriptionValid: Boolean
        abstract val newAccountBank: String
        abstract val isNewAccountBankValid: Boolean
        abstract val newAccountBankIcon: Int
        abstract fun onInitialBalanceChange(balance: String)
        abstract fun onDescriptionChange(description: String)
        abstract fun onBankChange(bankId: Int, bankName: String)
        abstract fun onSaveClick(showDialog: MutableState<Boolean>)
        abstract fun cleanNewAccount()
    }
}