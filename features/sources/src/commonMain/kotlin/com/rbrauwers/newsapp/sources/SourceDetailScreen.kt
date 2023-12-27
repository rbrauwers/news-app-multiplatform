package com.rbrauwers.newsapp.sources

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SourceDetailScreen(
    component: SourceDetailComponent,
    modifier: Modifier = Modifier
) {
    Text(text = component.source.name ?: "N/A", modifier = modifier)
}