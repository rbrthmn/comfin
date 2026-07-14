package br.com.rbrthmn.data.auth.provider

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import br.com.rbrthmn.data.auth.model.AuthResult
import br.com.rbrthmn.data.auth.model.AuthUser
import br.com.rbrthmn.data.auth.model.SignInMethod
import com.google.android.gms.tasks.Task
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

class FirebaseGoogleAuthProvider(
    private val credentialManager: CredentialManager,
    private val firebaseAuth: FirebaseAuth,
    private val serverClientId: String
) : ExternalAuthProvider {

    override val method: SignInMethod = SignInMethod.GOOGLE

    override suspend fun signIn(activityContext: Context): AuthResult = try {
        val idToken = requestGoogleIdToken(activityContext)
        if (idToken == null) {
            AuthResult.Error.Unknown()
        } else {
            signInWithFirebase(idToken)
        }
    } catch (cancellation: GetCredentialCancellationException) {
        AuthResult.Error.Cancelled
    } catch (unavailable: GetCredentialException) {
        AuthResult.Error.ProviderUnavailable
    } catch (failure: Exception) {
        AuthResult.Error.Unknown(failure)
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    private suspend fun requestGoogleIdToken(activityContext: Context): String? {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(serverClientId)
            .setFilterByAuthorizedAccounts(false)
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        val credential = credentialManager.getCredential(activityContext, request).credential
        if (credential !is CustomCredential ||
            credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            return null
        }
        return GoogleIdTokenCredential.createFrom(credential.data).idToken
    }

    private suspend fun signInWithFirebase(idToken: String): AuthResult {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        val firebaseUser = firebaseAuth.signInWithCredential(firebaseCredential).await().user
            ?: return AuthResult.Error.Unknown()
        return AuthResult.Success(
            AuthUser(
                id = firebaseUser.uid,
                name = firebaseUser.displayName.orEmpty(),
                email = firebaseUser.email.orEmpty()
            )
        )
    }

    private suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { continuation ->
        addOnCompleteListener { task ->
            val exception = task.exception
            if (exception != null) {
                continuation.resumeWithException(exception)
            } else {
                continuation.resume(task.result)
            }
        }
    }
}
