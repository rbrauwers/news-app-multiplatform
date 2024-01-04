package com.rbrauwers.newsapp.sources

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rbrauwers.newsapp.designsystem.BadgedTopBar
import com.rbrauwers.newsapp.designsystem.BottomBarState
import com.rbrauwers.newsapp.designsystem.InfoActionButton
import com.rbrauwers.newsapp.designsystem.LocalAppState
import com.rbrauwers.newsapp.designsystem.NewsAppDefaultProgressIndicator
import com.rbrauwers.newsapp.designsystem.SettingsActionButton
import com.rbrauwers.newsapp.designsystem.TopBarState
import com.rbrauwers.newsapp.model.NewsSource
import com.rbrauwers.newsapp.resources.MultiplatformResources
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun SourcesListScreen(
    component: SourcesListComponent,
    onNavigateToInfo: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSource: (NewsSource) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState: SourcesUiState by component.sourcesUiState.collectAsState()

    LocalAppState.current.apply {
        LaunchedEffect(uiState) {
            setTopBarState(
                topBarState = TopBarState(
                    title = {
                        BadgedTopBar(
                            title = stringResource(MultiplatformResources.strings.sources),
                            count = (uiState as? SourcesUiState.Success)?.sources?.size
                        )
                    },
                    actions =  {
                        InfoActionButton(onClick = onNavigateToInfo)
                        SettingsActionButton(onClick = onNavigateToSettings)
                    }
                )
            )
            setBottomBarState(bottomBarState = BottomBarState(isVisible = true))
        }
    }

    SourcesListScreenContent(
        uiState = uiState,
        modifier = modifier,
        onNavigateToSource = onNavigateToSource
    )
}

@Composable
private fun SourcesListScreenContent(
    uiState: SourcesUiState,
    onNavigateToSource: (NewsSource) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is SourcesUiState.Error -> {
            // TODO
        }

        is SourcesUiState.Loading -> {
            NewsAppDefaultProgressIndicator(placeOnCenter = true)
        }

        is SourcesUiState.Success -> {
            SourcesList(
                uiState = uiState,
                modifier = modifier,
                onNavigateToSource = onNavigateToSource
            )
        }
    }
}

@Composable
private fun SourcesList(
    uiState: SourcesUiState.Success,
    onNavigateToSource: (NewsSource) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = rememberLazyListState(),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        sources(
            sources = uiState.sources,
            onNavigateToSource = onNavigateToSource
        )
    }
}

private fun LazyListScope.sources(
    sources: List<NewsSource>,
    onNavigateToSource: (NewsSource) -> Unit,
) {
    itemsIndexed(
        items = sources,
        key = { _, source -> source.id }
    ) { index, source ->
        Row(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface)
                .clickable {
                    onNavigateToSource(source)
                },
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