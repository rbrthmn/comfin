package br.com.rbrthmn.settings.di

import br.com.rbrthmn.settings.SettingsScreenContract
import br.com.rbrthmn.settings.SettingsScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    viewModel<SettingsScreenContract.ViewModel> {
        SettingsScreenViewModel().doOnInit()
    }
}
