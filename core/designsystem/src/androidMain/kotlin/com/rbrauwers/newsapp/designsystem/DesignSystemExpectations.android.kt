package com.rbrauwers.newsapp.designsystem

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.rbrauwers.newsapp.designsystem.providers.CustomInsets
import com.rbrauwers.newsapp.designsystem.providers.PlatformInsetProvider

internal actual class PlatformInsetProviderImpl : PlatformInsetProvider {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    /**
     * TODO: Currently BottomSheetDefaults.windowInsets is zero on Android.
     * Therefore we need to add an arbitrary inset.
     * Check if it would be fixed in future versions.
     */
    override fun remember(): CustomInsets {
        val context = LocalContext.current
        val defaultBottomSheetInsets = BottomSheetDefaults.windowInsets

        return remember(context) {
            val customBottomSheetInsets = if (defaultBottomSheetInsets.getBottom(Density(context)) == 0) {
                defaultBottomSheetInsets.add(WindowInsets(bottom = 48.dp))
            } else defaultBottomSheetInsets
            CustomInsets(bottomSheetInsets = customBottomSheetInsets)
        }
    }

}

