package br.com.rbrthmn.data.auth.provider

import br.com.rbrthmn.data.auth.model.AuthResult
import br.com.rbrthmn.data.auth.model.SignInMethod

class GoogleAuthProvider : ExternalAuthProvider {
    override val method: SignInMethod = SignInMethod.GOOGLE

    override suspend fun signIn(): AuthResult = AuthResult.Error.ProviderUnavailable
}
