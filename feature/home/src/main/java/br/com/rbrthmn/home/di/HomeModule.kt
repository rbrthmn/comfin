package br.com.rbrthmn.home.di

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
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel<HomeScreenContract.ViewModel> {
        HomeScreenViewModel().doOnInit()
    }
    viewModel<MonthlyLimitCardContract.ViewModel> {
        MonthlyLimitCardViewModel(homeRepository = get()).doOnInit()
    }
    viewModel<BalanceCardContract.ViewModel> {
        BalanceCardViewModel(homeRepository = get()).doOnInit()
    }
    viewModel<LastMonthDifferenceCardContract.ViewModel> {
        LastMonthDifferenceCardViewModel(homeRepository = get()).doOnInit()
    }
    viewModel<CreditCardBillsCardContract.ViewModel> {
        CreditCardBillsCardViewModel(homeRepository = get()).doOnInit()
    }
}