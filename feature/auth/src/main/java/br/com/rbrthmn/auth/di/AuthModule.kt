package br.com.rbrthmn.auth.di

import br.com.rbrthmn.auth.passwordrecovery.PasswordRecoveryScreenContract
import br.com.rbrthmn.auth.passwordrecovery.PasswordRecoveryScreenViewModel
import br.com.rbrthmn.auth.signin.SignInScreenContract
import br.com.rbrthmn.auth.signin.SignInScreenViewModel
import br.com.rbrthmn.auth.signup.SignUpScreenContract
import br.com.rbrthmn.auth.signup.SignUpScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    viewModel<SignInScreenContract.ViewModel> {
        SignInScreenViewModel(authRepository = get()).doOnInit()
    }
    viewModel<SignUpScreenContract.ViewModel> {
        SignUpScreenViewModel(authRepository = get()).doOnInit()
    }
    viewModel<PasswordRecoveryScreenContract.ViewModel> {
        PasswordRecoveryScreenViewModel(authRepository = get()).doOnInit()
    }
}
