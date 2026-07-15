package br.com.rbrthmn.data.auth.di

import br.com.rbrthmn.data.auth.local.KeystoreSecureStorage
import br.com.rbrthmn.data.auth.local.LocalAuthDataSource
import br.com.rbrthmn.data.auth.local.PasswordHasher
import br.com.rbrthmn.data.auth.local.SecureStorage
import br.com.rbrthmn.data.auth.provider.ExternalAuthProvider
import br.com.rbrthmn.data.auth.provider.GoogleAuthProvider
import br.com.rbrthmn.data.auth.repository.AuthRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val authDataModule = module {
    single { PasswordHasher() }
    single<SecureStorage> { KeystoreSecureStorage(context = androidContext()) }
    single<ExternalAuthProvider> { GoogleAuthProvider() }
    single<AuthRepository> {
        LocalAuthDataSource(
            secureStorage = get(),
            passwordHasher = get(),
            externalAuthProviders = listOf(get())
        )
    }
}
