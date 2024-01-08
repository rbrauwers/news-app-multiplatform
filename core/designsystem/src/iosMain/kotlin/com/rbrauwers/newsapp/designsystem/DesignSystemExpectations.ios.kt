package com.rbrauwers.newsapp.designsystem

import androidx.compose.runtime.Composable
import com.rbrauwers.newsapp.designsystem.providers.CustomInsets
import com.rbrauwers.newsapp.designsystem.providers.PlatformInsetProvider
import androidx.compose.runtime.remember

internal actual class PlatformInsetProviderImpl : PlatformInsetProvider {

    @Composable
    override fun remember(): CustomInsets {
        return remember(Unit) {
            CustomInsets()
        }
    }

}