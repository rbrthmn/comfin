package br.com.rbrthmn.misc.di

import br.com.rbrthmn.misc.incomedivisions.IncomeDivisionsScreenContract
import br.com.rbrthmn.misc.incomedivisions.IncomeDivisionsScreenViewModel
import br.com.rbrthmn.misc.recurringexpenses.RecurringExpensesScreenContract
import br.com.rbrthmn.misc.recurringexpenses.RecurringExpensesScreenViewModel
import br.com.rbrthmn.misc.reserves.ReservesScreenContract
import br.com.rbrthmn.misc.reserves.ReservesScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val miscModule = module {
    viewModel<IncomeDivisionsScreenContract.ViewModel> {
        IncomeDivisionsScreenViewModel(stringProvider = get()).doOnInit()
    }
    viewModel<RecurringExpensesScreenContract.ViewModel> {
        RecurringExpensesScreenViewModel().doOnInit()
    }
    viewModel<ReservesScreenContract.ViewModel> {
        ReservesScreenViewModel().doOnInit()
    }
}
