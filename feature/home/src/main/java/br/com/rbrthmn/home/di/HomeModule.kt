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
import br.com.rbrthmn.ui.utils.DecimalFormatter
import br.com.rbrthmn.ui.utils.DecimalInputFieldFormatter
import br.com.rbrthmn.ui.utils.SnackBarProvider
import br.com.rbrthmn.ui.utils.SnackBarProviderImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val homeModule = module {
    single<DecimalInputFieldFormatter> { DecimalFormatter() }
    singleOf<SnackBarProvider>(::SnackBarProviderImpl)
    viewModel<HomeScreenContract.ViewModel> {
        HomeScreenViewModel().doOnInit()
    }
    viewModel<MonthlyLimitCardContract.ViewModel> {
        MonthlyLimitCardViewModel().doOnInit()
    }
    viewModel<BalanceCardContract.ViewModel> {
        BalanceCardViewModel().doOnInit()
    }
    viewModel<LastMonthDifferenceCardContract.ViewModel> {
        LastMonthDifferenceCardViewModel().doOnInit()
    }
    viewModel<CreditCardBillsCardContract.ViewModel> {
        CreditCardBillsCardViewModel().doOnInit()
    }
}