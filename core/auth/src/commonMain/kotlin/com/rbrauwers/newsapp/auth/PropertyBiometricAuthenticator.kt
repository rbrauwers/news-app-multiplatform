package com.rbrauwers.newsapp.auth

interface PropertyBiometricAuthenticator {

    suspend fun authenticate(
        specs: PlatformBiometricAuthenticatorSpecs,
        title: String = "Authentication required",
        subtitle: String = "Log in using your biometric credential",
        errorButton: String = "Error"
    ) : BiometricStatus

}