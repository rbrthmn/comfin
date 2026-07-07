package br.com.rbrthmn.data.auth

import br.com.rbrthmn.data.auth.local.PasswordHasher
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class PasswordHasherTest {
    private val hasher = PasswordHasher()

    @Test
    fun `hash with same password and salt should be deterministic`() {
        val salt = hasher.generateSalt()

        assertEquals(hasher.hash(PASSWORD, salt), hasher.hash(PASSWORD, salt))
    }

    @Test
    fun `hash with different salts should produce different hashes`() {
        val firstHash = hasher.hash(PASSWORD, hasher.generateSalt())
        val secondHash = hasher.hash(PASSWORD, hasher.generateSalt())

        assertFalse(firstHash == secondHash)
    }

    @Test
    fun `verify with correct password should return true`() {
        val salt = hasher.generateSalt()
        val hash = hasher.hash(PASSWORD, salt)

        assertTrue(hasher.verify(PASSWORD, salt, hash))
    }

    @Test
    fun `verify with wrong password should return false`() {
        val salt = hasher.generateSalt()
        val hash = hasher.hash(PASSWORD, salt)

        assertFalse(hasher.verify(WRONG_PASSWORD, salt, hash))
    }

    private companion object {
        const val PASSWORD = "password123"
        const val WRONG_PASSWORD = "wrongpassword"
    }
}
