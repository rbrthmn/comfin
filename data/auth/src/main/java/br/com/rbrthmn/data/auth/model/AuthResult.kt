package br.com.rbrthmn.data.auth.model

sealed class AuthResult {
    data class Success(val user: AuthUser) : AuthResult()

    sealed class Error : AuthResult() {
        object InvalidCredentials : Error()
        object EmailAlreadyRegistered : Error()
        object AccountNotFound : Error()
        object ProviderUnavailable : Error()
        object Cancelled : Error()
        data class Unknown(val cause: Throwable? = null) : Error()
    }
}
