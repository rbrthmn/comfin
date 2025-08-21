package br.com.rbrthmn.operations.di

import br.com.rbrthmn.operations.ui.OperationsScreenContract
import br.com.rbrthmn.operations.ui.OperationsScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val operationsModule = module {
    viewModel<OperationsScreenContract.ViewModel> {
        OperationsScreenViewModel(stringProvider = get()).doOnInit()
    }
}
