package com.rbrauwers.newsapp.auth

import androidx.compose.runtime.Composable
import dev.icerock.moko.biometry.BiometryAuthenticator
import dev.icerock.moko.biometry.compose.BiometryAuthenticatorFactory

@Composable
actual fun BiometryAuthenticator.prepare(): BiometryAuthenticator {
    return this
}

@Composable
actual fun rememberBiometryAuthenticator(factory: BiometryAuthenticatorFactory): BiometryAuthenticator {
    return factory.createBiometryAuthenticator()
}