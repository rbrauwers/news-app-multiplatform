package com.rbrauwers.newsapp.designsystem.providers

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable

interface PlatformInsetProvider {
    @Composable
    fun remember(): CustomInsets
}

data class CustomInsets(
    val bottomSheetInsets: WindowInsets = WindowInsets(
        left = 0,
        top = 0,
        right = 0,
        bottom = 0
    )
)
