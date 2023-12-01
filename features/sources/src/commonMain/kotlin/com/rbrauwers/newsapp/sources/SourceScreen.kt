package com.rbrauwers.newsapp.sources

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rbrauwers.newsapp.designsystem.NewsAppDefaultProgressIndicator
import com.rbrauwers.newsapp.model.NewsSource

@Composable
fun SourceScreen(component: SourcesComponent) {
    val uiState: SourceUiState by component.sourceUiState.collectAsState()
    SourcesScreenContent(uiState = uiState)
}

@Composable
private fun SourcesScreenContent(
    uiState: SourceUiState
) {
    when (uiState) {
        is SourceUiState.Error -> {
            // TODO
        }

        is SourceUiState.Loading -> {
            NewsAppDefaultProgressIndicator(placeOnCenter = true)
        }

        is SourceUiState.Success -> {
            SourcesList(uiState)
        }
    }
}

@Composable
private fun SourcesList(uiState: SourceUiState.Success) {
    LazyColumn(
        state = rememberLazyListState(),
        horizontalAlignment = Alignment.CenterHorizontally
    )  {
        sources(sources = uiState.sources)
    }
}

private fun LazyListScope.sources(sources: List<NewsSource>) {
    itemsIndexed(
        items = sources,
        key = { _, source -> source.id }
    ) { index, source ->
        Row(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = source.name.orEmpty(),
                    maxLines = 1,
                    style = MaterialTheme.typography.headlineSmall
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = source.category.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = source.language.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (index != sources.lastIndex) {
            Divider()
        }
    }
}