package br.com.rbrthmn.misc.di

import br.com.rbrthmn.misc.reserves.ReservesContract
import br.com.rbrthmn.misc.reserves.ReservesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val miscModule = module {
    viewModel<ReservesContract.ViewModel> {
        ReservesViewModel().doOnInit()
    }
}