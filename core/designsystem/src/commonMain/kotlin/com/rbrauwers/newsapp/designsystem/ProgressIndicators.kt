package com.rbrauwers.newsapp.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NewsAppDefaultProgressIndicator(
    modifier: Modifier = Modifier,
    placeOnCenter: Boolean = false
) {
    @Composable
    fun Build(modifier: Modifier) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp,
            modifier = modifier.size(48.dp)
        )
    }

    if (placeOnCenter) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Build(modifier)
        }
    } else {
        Build(modifier)
    }
}

fun LazyListScope.newsAppDefaultProgressIndicatorItem(
    modifier: Modifier = Modifier,
    placeOnCenter: Boolean = false
) {
    item {
        NewsAppDefaultProgressIndicator(modifier = modifier, placeOnCenter = placeOnCenter)
    }
}
