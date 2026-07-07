package br.com.rbrthmn.data.auth.local

import br.com.rbrthmn.data.auth.model.AuthResult
import br.com.rbrthmn.data.auth.model.AuthUser
import br.com.rbrthmn.data.auth.model.SignInMethod
import br.com.rbrthmn.data.auth.provider.ExternalAuthProvider
import br.com.rbrthmn.data.auth.repository.AuthRepository
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalAuthDataSource(
    private val secureStorage: SecureStorage,
    private val passwordHasher: PasswordHasher,
    private val externalAuthProviders: List<ExternalAuthProvider> = emptyList()
) : AuthRepository {

    override val currentUser: AuthUser?
        get() = if (isAuthenticated()) readUser() else null

    override fun isAuthenticated(): Boolean =
        secureStorage.getString(KEY_SESSION_ACTIVE) == SESSION_ACTIVE

    override suspend fun signUp(name: String, email: String, password: String): AuthResult =
        withContext(Dispatchers.Default) {
            val storedEmail = secureStorage.getString(KEY_USER_EMAIL)
            if (storedEmail != null && storedEmail.equals(email, ignoreCase = true)) {
                return@withContext AuthResult.Error.EmailAlreadyRegistered
            }
            val user = AuthUser(id = UUID.randomUUID().toString(), name = name, email = email)
            val salt = passwordHasher.generateSalt()
            writeUser(user)
            secureStorage.putString(KEY_PASSWORD_SALT, salt)
            secureStorage.putString(KEY_PASSWORD_HASH, passwordHasher.hash(password, salt))
            secureStorage.putString(KEY_SESSION_ACTIVE, SESSION_ACTIVE)
            AuthResult.Success(user)
        }

    override suspend fun signIn(email: String, password: String): AuthResult =
        withContext(Dispatchers.Default) {
            val user = readUser() ?: return@withContext AuthResult.Error.InvalidCredentials
            val salt = secureStorage.getString(KEY_PASSWORD_SALT)
            val hash = secureStorage.getString(KEY_PASSWORD_HASH)
            val credentialsMatch = user.email.equals(email, ignoreCase = true) &&
                    salt != null && hash != null &&
                    passwordHasher.verify(password, salt, hash)
            if (!credentialsMatch) return@withContext AuthResult.Error.InvalidCredentials
            secureStorage.putString(KEY_SESSION_ACTIVE, SESSION_ACTIVE)
            AuthResult.Success(user)
        }

    override suspend fun signInWith(method: SignInMethod): AuthResult {
        val provider = externalAuthProviders.firstOrNull { it.method == method }
            ?: return AuthResult.Error.ProviderUnavailable
        val result = provider.signIn()
        if (result is AuthResult.Success) {
            writeUser(result.user)
            secureStorage.putString(KEY_SESSION_ACTIVE, SESSION_ACTIVE)
        }
        return result
    }

    override suspend fun verifyAccountEmail(email: String): AuthResult {
        val user = readUser() ?: return AuthResult.Error.AccountNotFound
        if (!user.email.equals(email, ignoreCase = true)) return AuthResult.Error.AccountNotFound
        return AuthResult.Success(user)
    }

    override suspend fun resetPassword(email: String, newPassword: String): AuthResult =
        withContext(Dispatchers.Default) {
            val verification = verifyAccountEmail(email)
            if (verification !is AuthResult.Success) return@withContext verification
            val salt = passwordHasher.generateSalt()
            secureStorage.putString(KEY_PASSWORD_SALT, salt)
            secureStorage.putString(KEY_PASSWORD_HASH, passwordHasher.hash(newPassword, salt))
            verification
        }

    override fun signOut() {
        secureStorage.remove(KEY_SESSION_ACTIVE)
    }

    private fun readUser(): AuthUser? {
        val id = secureStorage.getString(KEY_USER_ID) ?: return null
        val name = secureStorage.getString(KEY_USER_NAME) ?: return null
        val email = secureStorage.getString(KEY_USER_EMAIL) ?: return null
        return AuthUser(id = id, name = name, email = email)
    }

    private fun writeUser(user: AuthUser) {
        secureStorage.putString(KEY_USER_ID, user.id)
        secureStorage.putString(KEY_USER_NAME, user.name)
        secureStorage.putString(KEY_USER_EMAIL, user.email)
    }

    companion object {
        const val KEY_USER_ID = "auth_user_id"
        const val KEY_USER_NAME = "auth_user_name"
        const val KEY_USER_EMAIL = "auth_user_email"
        const val KEY_PASSWORD_SALT = "auth_password_salt"
        const val KEY_PASSWORD_HASH = "auth_password_hash"
        const val KEY_SESSION_ACTIVE = "auth_session_active"
        const val SESSION_ACTIVE = "true"
    }
}
