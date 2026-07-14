package br.com.rbrthmn.data.auth.provider

import android.content.Context
import br.com.rbrthmn.data.auth.model.AuthResult
import br.com.rbrthmn.data.auth.model.SignInMethod

interface ExternalAuthProvider {
    val method: SignInMethod

    suspend fun signIn(activityContext: Context): AuthResult

    fun signOut()
}
