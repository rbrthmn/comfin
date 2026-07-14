package br.com.rbrthmn.data.auth

import android.content.Context
import br.com.rbrthmn.data.auth.local.LocalAuthDataSource
import br.com.rbrthmn.data.auth.local.PasswordHasher
import br.com.rbrthmn.data.auth.local.SecureStorage
import br.com.rbrthmn.data.auth.model.AuthResult
import br.com.rbrthmn.data.auth.model.AuthUser
import br.com.rbrthmn.data.auth.model.SignInMethod
import br.com.rbrthmn.data.auth.provider.ExternalAuthProvider
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LocalAuthDataSourceTest {
    private val secureStorage = FakeSecureStorage()
    private val activityContext = mockk<Context>()
    private val dataSource = createDataSource()

    private fun createDataSource(vararg providers: ExternalAuthProvider) = LocalAuthDataSource(
        secureStorage = secureStorage,
        passwordHasher = PasswordHasher(),
        externalAuthProviders = providers.toList()
    )

    @Test
    fun `signUp with new email should store user and start session`() = runTest {
        val result = dataSource.signUp(name = NAME, email = EMAIL, password = PASSWORD)

        assertTrue(result is AuthResult.Success)
        assertTrue(dataSource.isAuthenticated())
        assertEquals(EMAIL, dataSource.currentUser?.email)
    }

    @Test
    fun `signUp with already registered email should return EmailAlreadyRegistered`() = runTest {
        dataSource.signUp(name = NAME, email = EMAIL, password = PASSWORD)

        val result = dataSource.signUp(name = NAME, email = EMAIL, password = OTHER_PASSWORD)

        assertEquals(AuthResult.Error.EmailAlreadyRegistered, result)
    }

    @Test
    fun `signIn with correct credentials should return Success and start session`() = runTest {
        dataSource.signUp(name = NAME, email = EMAIL, password = PASSWORD)
        dataSource.signOut()

        val result = dataSource.signIn(email = EMAIL, password = PASSWORD)

        assertTrue(result is AuthResult.Success)
        assertTrue(dataSource.isAuthenticated())
    }

    @Test
    fun `signIn with wrong password should return InvalidCredentials`() = runTest {
        dataSource.signUp(name = NAME, email = EMAIL, password = PASSWORD)
        dataSource.signOut()

        val result = dataSource.signIn(email = EMAIL, password = OTHER_PASSWORD)

        assertEquals(AuthResult.Error.InvalidCredentials, result)
        assertFalse(dataSource.isAuthenticated())
    }

    @Test
    fun `signIn with no stored account should return InvalidCredentials`() = runTest {
        val result = dataSource.signIn(email = EMAIL, password = PASSWORD)

        assertEquals(AuthResult.Error.InvalidCredentials, result)
    }

    @Test
    fun `signInWith with no provider for method should return ProviderUnavailable`() = runTest {
        val result = dataSource.signInWith(SignInMethod.GOOGLE, activityContext)

        assertEquals(AuthResult.Error.ProviderUnavailable, result)
        assertFalse(dataSource.isAuthenticated())
    }

    @Test
    fun `signInWith GOOGLE with successful provider should store user and start session`() =
        runTest {
            val provider = FakeExternalAuthProvider(result = AuthResult.Success(GOOGLE_USER))
            val dataSource = createDataSource(provider)

            val result = dataSource.signInWith(SignInMethod.GOOGLE, activityContext)

            assertTrue(result is AuthResult.Success)
            assertTrue(dataSource.isAuthenticated())
            assertEquals(GOOGLE_USER.email, dataSource.currentUser?.email)
        }

    @Test
    fun `signInWith GOOGLE with cancelled provider should return Cancelled and not start session`() =
        runTest {
            val provider = FakeExternalAuthProvider(result = AuthResult.Error.Cancelled)
            val dataSource = createDataSource(provider)

            val result = dataSource.signInWith(SignInMethod.GOOGLE, activityContext)

            assertEquals(AuthResult.Error.Cancelled, result)
            assertFalse(dataSource.isAuthenticated())
        }

    @Test
    fun `signOut should sign out external providers`() = runTest {
        val provider = FakeExternalAuthProvider(result = AuthResult.Success(GOOGLE_USER))
        val dataSource = createDataSource(provider)
        dataSource.signInWith(SignInMethod.GOOGLE, activityContext)

        dataSource.signOut()

        assertTrue(provider.signedOut)
        assertFalse(dataSource.isAuthenticated())
    }

    @Test
    fun `verifyAccountEmail with unknown email should return AccountNotFound`() = runTest {
        val result = dataSource.verifyAccountEmail(email = EMAIL)

        assertEquals(AuthResult.Error.AccountNotFound, result)
    }

    @Test
    fun `verifyAccountEmail with stored email should return Success`() = runTest {
        dataSource.signUp(name = NAME, email = EMAIL, password = PASSWORD)

        val result = dataSource.verifyAccountEmail(email = EMAIL)

        assertTrue(result is AuthResult.Success)
    }

    @Test
    fun `resetPassword with stored email should allow sign in with new password`() = runTest {
        dataSource.signUp(name = NAME, email = EMAIL, password = PASSWORD)
        dataSource.signOut()

        val result = dataSource.resetPassword(email = EMAIL, newPassword = OTHER_PASSWORD)

        assertTrue(result is AuthResult.Success)
        assertEquals(
            AuthResult.Error.InvalidCredentials,
            dataSource.signIn(email = EMAIL, password = PASSWORD)
        )
        assertTrue(dataSource.signIn(email = EMAIL, password = OTHER_PASSWORD) is AuthResult.Success)
    }

    @Test
    fun `signOut should end session`() = runTest {
        dataSource.signUp(name = NAME, email = EMAIL, password = PASSWORD)

        dataSource.signOut()

        assertFalse(dataSource.isAuthenticated())
    }

    private class FakeExternalAuthProvider(
        private val result: AuthResult,
        override val method: SignInMethod = SignInMethod.GOOGLE
    ) : ExternalAuthProvider {
        var signedOut = false

        override suspend fun signIn(activityContext: Context): AuthResult = result

        override fun signOut() {
            signedOut = true
        }
    }

    private class FakeSecureStorage : SecureStorage {
        private val values = mutableMapOf<String, String>()

        override fun putString(key: String, value: String) {
            values[key] = value
        }

        override fun getString(key: String): String? = values[key]

        override fun remove(vararg keys: String) {
            keys.forEach(values::remove)
        }
    }

    private companion object {
        const val NAME = "User"
        const val EMAIL = "user@email.com"
        const val PASSWORD = "password123"
        const val OTHER_PASSWORD = "newpassword456"
        val GOOGLE_USER = AuthUser(id = "google-id", name = "Google User", email = "google@email.com")
    }
}
