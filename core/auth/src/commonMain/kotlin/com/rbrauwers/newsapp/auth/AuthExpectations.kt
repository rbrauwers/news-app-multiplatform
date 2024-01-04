package com.rbrauwers.newsapp.auth

import androidx.compose.runtime.Composable
import dev.icerock.moko.biometry.BiometryAuthenticator
import dev.icerock.moko.biometry.compose.BiometryAuthenticatorFactory

@Composable
expect fun BiometryAuthenticator.prepare(): BiometryAuthenticator

@Composable
expect fun rememberBiometryAuthenticator(factory: BiometryAuthenticatorFactory): BiometryAuthenticator