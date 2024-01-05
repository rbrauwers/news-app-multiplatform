package com.rbrauwers.newsapp.headlines

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rbrauwers.newsapp.designsystem.BadgedTopBar
import com.rbrauwers.newsapp.designsystem.BottomBarState
import com.rbrauwers.newsapp.designsystem.InfoActionButton
import com.rbrauwers.newsapp.designsystem.LocalAppState
import com.rbrauwers.newsapp.designsystem.NewsAppDefaultProgressIndicator
import com.rbrauwers.newsapp.designsystem.SettingsActionButton
import com.rbrauwers.newsapp.designsystem.TopBarState
import com.rbrauwers.newsapp.resources.MultiplatformResources
import dev.icerock.moko.resources.compose.stringResource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import openUrl

@Composable
fun HeadlineScreen(
    component: HeadlinesComponent,
    onNavigateToInfo: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState: HeadlineUiState by component.headlineUiState.collectAsState()

    LocalAppState.current.apply {
        LaunchedEffect(uiState) {
            setTopBarState(
                topBarState = TopBarState(
                    title = {
                        BadgedTopBar(
                            title = stringResource(MultiplatformResources.strings.headlines),
                            count = (uiState as? HeadlineUiState.Success)?.headlines?.size
                        )
                    },
                    actions = {
                        InfoActionButton(onClick = onNavigateToInfo)
                        SettingsActionButton(onClick = onNavigateToSettings)
                    }
                )
            )

            setBottomBarState(bottomBarState = BottomBarState(isVisible = true))
        }
    }

    HeadlineScreenContent(
        uiState = uiState,
        onLikedChanged = { article, liked ->
            component.updateLiked(article = article, liked)
        },
        modifier = modifier
    )
}

@Composable
private fun HeadlineScreenContent(
    uiState: HeadlineUiState,
    onLikedChanged: (ArticleUi, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is HeadlineUiState.Error -> {
            // TODO
        }

        is HeadlineUiState.Loading -> {
            NewsAppDefaultProgressIndicator(placeOnCenter = true)
        }

        is HeadlineUiState.Success -> {
            HeadlinesList(
                uiState = uiState,
                onLikedChanged = onLikedChanged,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun HeadlinesList(
    listState: LazyListState = rememberLazyListState(),
    uiState: HeadlineUiState.Success,
    onLikedChanged: (ArticleUi, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
    ) {
        headlines(
            headlines = uiState.headlines,
            onLikedChanged = onLikedChanged
        )
    }
}

private fun LazyListScope.headlines(
    headlines: List<ArticleUi>,
    onLikedChanged: (ArticleUi, Boolean) -> Unit
) {
    itemsIndexed(
        items = headlines,
        key = { _, article -> article.id }
    ) { index, article ->
        Headline(
            article = article,
            isFirst = index == 0,
            isLast = index == headlines.lastIndex,
            onLikedChanged = onLikedChanged
        )
    }
}

@Composable
internal fun Headline(
    article: ArticleUi,
    isFirst: Boolean = false,
    isLast: Boolean = false,
    onLikedChanged: (ArticleUi, Boolean) -> Unit
) {
    val imageShape = RoundedCornerShape(8.dp)

    Card(
        modifier = Modifier
            .height(216.dp)
            .fillMaxWidth()
            .padding(
                top = if (isFirst) 12.dp else 4.dp,
                bottom = if (isLast) 12.dp else 4.dp,
                start = 16.dp,
                end = 16.dp
            )
            .clickable {
                openUrl(url = article.url)
            }
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(24.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = article.author.orEmpty(),
                    maxLines = 1,
                    style = MaterialTheme.typography.titleSmall,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                )

                Text(
                    text = article.title.orEmpty(),
                    maxLines = 3,
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                article.formattedPublishedAt?.let { date ->
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.onPrimary,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                when (val url = article.urlToImage) {
                    null -> {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                        )
                    }

                    else -> {
                        KamelImage(
                            resource = asyncPainterResource(data = url),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            onFailure = { },
                            modifier = Modifier
                                .size(70.dp)
                                .clip(imageShape)
                                .border(
                                    border = BorderStroke(width = 1.dp, color = Color.Black),
                                    shape = imageShape
                                )
                                .background(Color.Gray)
                        )
                    }
                }

                FilledIconToggleButton(
                    checked = article.liked,
                    onCheckedChange = { isChecked ->
                        onLikedChanged(article, isChecked)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "")
                }
            }
        }

    }
}