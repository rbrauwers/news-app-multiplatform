package com.rbrauwers.newsapp.auth

import androidx.compose.runtime.Composable
import dev.icerock.moko.biometry.BiometryAuthenticator
import dev.icerock.moko.biometry.compose.BiometryAuthenticatorFactory
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthenticationWithBiometrics
import kotlin.coroutines.resume

@Composable
actual fun BiometryAuthenticator.prepare(): BiometryAuthenticator {
    return this
}

@Composable
actual fun rememberBiometryAuthenticator(factory: BiometryAuthenticatorFactory): BiometryAuthenticator {
    return factory.createBiometryAuthenticator()
}

actual class PlatformBiometricAuthenticatorSpecs

actual class PlatformBiometricAuthenticator : PropertyBiometricAuthenticator {

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    override suspend fun authenticate(
        specs: PlatformBiometricAuthenticatorSpecs,
        title: String,
        subtitle: String,
        errorButton: String
    ): BiometricStatus {
        val context = LAContext()

        val (canEvaluate: Boolean?, error: NSError?) = memScoped {
            val p = alloc<ObjCObjectVar<NSError?>>()
            val canEvaluate: Boolean? = runCatching {
                context.canEvaluatePolicy(
                    LAPolicyDeviceOwnerAuthenticationWithBiometrics,
                    error = p.ptr
                )
            }.getOrNull()
            canEvaluate to p.value
        }

        if (error != null) {
            return BiometricStatus.Error(error = error.localizedDescription)
        }

        if (canEvaluate == null || canEvaluate == false) {
            return BiometricStatus.NotAvailable
        }

        return suspendCancellableCoroutine { continuation ->
            context.evaluatePolicy(
                policy = LAPolicyDeviceOwnerAuthenticationWithBiometrics,
                localizedReason = title
            ) { success, authenticationError ->
                if (success) {
                    continuation.resume(BiometricStatus.Success)
                } else {
                    continuation.resume(BiometricStatus.Error(error = authenticationError?.localizedDescription))
                }
            }
        }
    }

}

@Composable
actual fun rememberPlatformBiometricAuthenticatorSpecs(): PlatformBiometricAuthenticatorSpecs {
    return PlatformBiometricAuthenticatorSpecs()
}