package com.rbrauwers.newsapp.auth

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import dev.icerock.moko.biometry.BiometryAuthenticator
import dev.icerock.moko.biometry.compose.BiometryAuthenticatorFactory
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@Composable
actual fun BiometryAuthenticator.prepare(): BiometryAuthenticator = apply {
    val activity = LocalContext.current.findActivity() as FragmentActivity
    bind(lifecycle = activity.lifecycle, fragmentManager = activity.supportFragmentManager)
}

@Composable
actual fun rememberBiometryAuthenticator(factory: BiometryAuthenticatorFactory): BiometryAuthenticator {
    return remember(factory) {
        factory.createBiometryAuthenticator()
    }.also {
        it.prepare()
    }
}

actual class PlatformBiometricAuthenticatorSpecs(val activity: FragmentActivity)

actual class PlatformBiometricAuthenticator : PropertyBiometricAuthenticator {

    override suspend fun authenticate(
        specs: PlatformBiometricAuthenticatorSpecs,
        title: String,
        subtitle: String,
        errorButton: String
    ): BiometricStatus {
        return suspendCancellableCoroutine { continuation ->
            val biometricManager = BiometricManager.from(specs.activity)
            val canAuthenticate = biometricManager
                .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS

            if (!canAuthenticate) {
                continuation.resume(BiometricStatus.NotAvailable)
                return@suspendCancellableCoroutine
            }

            val biometricPrompt = BiometricPrompt(
                specs.activity,
                ContextCompat.getMainExecutor(specs.activity),
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        continuation.resume(BiometricStatus.Success)
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        continuation.resume(BiometricStatus.Error(errString.toString()))
                    }
                }
            )

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
    }
}

@Composable
actual fun rememberPlatformBiometricAuthenticatorSpecs(): PlatformBiometricAuthenticatorSpecs {
    val context = LocalContext.current
    return remember(context) {
        val activity = context.findActivity() as FragmentActivity
        PlatformBiometricAuthenticatorSpecs(activity = activity)
    }
}

private fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}