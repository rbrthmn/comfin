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

package br.com.rbrthmn.data.di

import br.com.rbrthmn.ui.financialcompanion.screens.home.components.balancecard.BalanceCardContract
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.creditcardbillscard.CreditCardBillsCardContract
import br.com.rbrthmn.ui.financialcompanion.utils.DecimalFormatter
import br.com.rbrthmn.ui.financialcompanion.utils.DecimalInputFieldFormatter
import br.com.rbrthmn.ui.financialcompanion.utils.SnackBarProvider
import br.com.rbrthmn.ui.financialcompanion.utils.SnackBarProviderImpl
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.balancecard.BalanceCardViewModel
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.creditcardbillscard.CreditCardBillsCardViewModel
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.lastmonthdifferencecard.LastMonthDifferenceCardContract
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.lastmonthdifferencecard.LastMonthDifferenceCardViewModel
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.monthlylimitcard.MonthlyLimitCardContract
import br.com.rbrthmn.ui.financialcompanion.screens.home.components.monthlylimitcard.MonthlyLimitCardViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf<BalanceCardContract.BalanceCardViewModel>(::BalanceCardViewModel)
    viewModelOf<CreditCardBillsCardContract.CreditCardsBillCardViewModel>(::CreditCardBillsCardViewModel)
    viewModelOf<LastMonthDifferenceCardContract.LastMonthDifferenceCardViewModel>(::LastMonthDifferenceCardViewModel)
    viewModelOf<MonthlyLimitCardContract.MonthlyLimitCardViewModel>(::MonthlyLimitCardViewModel)

    factoryOf<DecimalInputFieldFormatter>(::DecimalFormatter)

    singleOf<SnackBarProvider>(::SnackBarProviderImpl)
}
