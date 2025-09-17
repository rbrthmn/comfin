package br.com.rbrthmn.settings.di

import br.com.rbrthmn.settings.ui.SettingsContract
import br.com.rbrthmn.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    viewModel<SettingsContract.ViewModel> {
        SettingsViewModel().doOnInit()
    }
}
