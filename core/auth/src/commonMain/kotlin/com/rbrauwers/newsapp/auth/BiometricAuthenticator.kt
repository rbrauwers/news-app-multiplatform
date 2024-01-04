package com.rbrauwers.newsapp.auth

import dev.icerock.moko.biometry.BiometryAuthenticator
import dev.icerock.moko.resources.desc.Raw
import dev.icerock.moko.resources.desc.StringDesc
import kotlin.coroutines.CoroutineContext

interface BiometricAuthenticator {

    suspend fun authenticate(
        biometryAuthenticator: BiometryAuthenticator,
        title: String = "Authentication required",
        subtitle: String = "Log in using your biometric credential",
        errorButton: String = "Error"
    ) : BiometricStatus

    sealed interface BiometricStatus {
        data object Failure : BiometricStatus
        data object NotRequested : BiometricStatus
        data object NotAvailable : BiometricStatus
        data object Success : BiometricStatus
        class Error(val error: String?) : BiometricStatus
    }

}

internal class DefaultBiometricAuthenticator(mainContext: CoroutineContext) :
    BiometricAuthenticator {

    override suspend fun authenticate(
        biometryAuthenticator: BiometryAuthenticator,
        title: String,
        subtitle: String,
        errorButton: String
    ): BiometricAuthenticator.BiometricStatus {
        return try {
            if (!biometryAuthenticator.isBiometricAvailable()) {
                return BiometricAuthenticator.BiometricStatus.NotAvailable
            }

            val isSuccess = biometryAuthenticator.checkBiometryAuthentication(
                requestTitle = StringDesc.Raw(title),
                requestReason = StringDesc.Raw(subtitle),
                failureButtonText = StringDesc.Raw(errorButton)
            )

            if (isSuccess) BiometricAuthenticator.BiometricStatus.Success else BiometricAuthenticator.BiometricStatus.Failure
        } catch (e: Throwable) {
            BiometricAuthenticator.BiometricStatus.Error(error = e.message)
        }
    }

}

