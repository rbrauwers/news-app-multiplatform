package com.rbrauwers.newsapp.auth

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import dev.icerock.moko.biometry.BiometryAuthenticator
import dev.icerock.moko.biometry.compose.BiometryAuthenticatorFactory

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

private fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}