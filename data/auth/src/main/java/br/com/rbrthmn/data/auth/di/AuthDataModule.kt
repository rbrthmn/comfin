package br.com.rbrthmn.data.auth.di

import android.content.Context
import androidx.credentials.CredentialManager
import br.com.rbrthmn.data.auth.local.KeystoreSecureStorage
import br.com.rbrthmn.data.auth.local.LocalAuthDataSource
import br.com.rbrthmn.data.auth.local.PasswordHasher
import br.com.rbrthmn.data.auth.local.SecureStorage
import br.com.rbrthmn.data.auth.provider.ExternalAuthProvider
import br.com.rbrthmn.data.auth.provider.FirebaseGoogleAuthProvider
import br.com.rbrthmn.data.auth.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val authDataModule = module {
    single { PasswordHasher() }
    single<SecureStorage> { KeystoreSecureStorage(context = androidContext()) }
    single<ExternalAuthProvider> {
        FirebaseGoogleAuthProvider(
            credentialManager = CredentialManager.create(androidContext()),
            firebaseAuth = FirebaseAuth.getInstance(),
            serverClientId = androidContext().defaultWebClientId()
        )
    }
    single<AuthRepository> {
        LocalAuthDataSource(
            secureStorage = get(),
            passwordHasher = get(),
            externalAuthProviders = listOf(get())
        )
    }
}

private fun Context.defaultWebClientId(): String {
    val resId = resources.getIdentifier("default_web_client_id", "string", packageName)
    return if (resId != 0) getString(resId) else ""
}
