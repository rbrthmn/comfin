package br.com.rbrthmn.data.auth.repository

import br.com.rbrthmn.data.auth.model.AuthResult
import br.com.rbrthmn.data.auth.model.AuthUser
import br.com.rbrthmn.data.auth.model.SignInMethod

interface AuthRepository {
    val currentUser: AuthUser?

    fun isAuthenticated(): Boolean

    suspend fun signUp(name: String, email: String, password: String): AuthResult

    suspend fun signIn(email: String, password: String): AuthResult

    suspend fun signInWith(method: SignInMethod): AuthResult

    suspend fun verifyAccountEmail(email: String): AuthResult

    suspend fun resetPassword(email: String, newPassword: String): AuthResult

    fun signOut()
}
