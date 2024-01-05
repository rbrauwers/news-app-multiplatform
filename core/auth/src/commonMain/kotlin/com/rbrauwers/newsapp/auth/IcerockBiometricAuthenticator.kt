package com.rbrauwers.newsapp.auth

import dev.icerock.moko.biometry.BiometryAuthenticator
import dev.icerock.moko.resources.desc.Raw
import dev.icerock.moko.resources.desc.StringDesc

interface IcerockBiometricAuthenticator {

    suspend fun authenticate(
        biometryAuthenticator: BiometryAuthenticator,
        title: String = "Authentication required",
        subtitle: String = "Log in using your biometric credential",
        errorButton: String = "Error"
    ) : BiometricStatus

}

class DefaultIcerockBiometricAuthenticator : IcerockBiometricAuthenticator {

    override suspend fun authenticate(
        biometryAuthenticator: BiometryAuthenticator,
        title: String,
        subtitle: String,
        errorButton: String
    ): BiometricStatus {
        return try {
            if (!biometryAuthenticator.isBiometricAvailable()) {
                return BiometricStatus.NotAvailable
            }

            val isSuccess = biometryAuthenticator.checkBiometryAuthentication(
                requestTitle = StringDesc.Raw(title),
                requestReason = StringDesc.Raw(subtitle),
                failureButtonText = StringDesc.Raw(errorButton)
            )

            if (isSuccess) BiometricStatus.Success else BiometricStatus.Failure
        } catch (e: Throwable) {
            BiometricStatus.Error(error = e.message)
        }
    }

}

