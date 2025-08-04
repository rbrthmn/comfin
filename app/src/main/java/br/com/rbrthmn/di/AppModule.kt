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

package br.com.rbrthmn.di

import br.com.rbrthmn.home.ui.HomeScreenContract
import br.com.rbrthmn.home.ui.HomeScreenViewModel
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardContract
import br.com.rbrthmn.home.ui.components.balancecard.BalanceCardViewModel
import br.com.rbrthmn.home.ui.components.creditcardbillscard.CreditCardBillsCardContract
import br.com.rbrthmn.home.ui.components.creditcardbillscard.CreditCardBillsCardViewModel
import br.com.rbrthmn.home.ui.components.lastmonthdifferencecard.LastMonthDifferenceCardContract
import br.com.rbrthmn.home.ui.components.lastmonthdifferencecard.LastMonthDifferenceCardViewModel
import br.com.rbrthmn.home.ui.components.monthlylimitcard.MonthlyLimitCardContract
import br.com.rbrthmn.home.ui.components.monthlylimitcard.MonthlyLimitCardViewModel
import br.com.rbrthmn.operations.OperationsScreenContract
import br.com.rbrthmn.operations.OperationsScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf<HomeScreenContract.ViewModel>(::HomeScreenViewModel)
    viewModel<BalanceCardContract.ViewModel> {
        BalanceCardViewModel().doOnInit()
    }
    viewModel<CreditCardBillsCardContract.ViewModel> {
        CreditCardBillsCardViewModel().doOnInit()
    }
    viewModel<LastMonthDifferenceCardContract.ViewModel> {
        LastMonthDifferenceCardViewModel().doOnInit()
    }
    viewModel<MonthlyLimitCardContract.ViewModel> {
        MonthlyLimitCardViewModel().doOnInit()
    }
    viewModel<OperationsScreenContract.ViewModel> {
        OperationsScreenViewModel(stringProvider = get()).doOnInit()
    }
}
