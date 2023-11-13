package com.rbrauwers.newsapp.headlines

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.rbrauwers.newsapp.designsystem.NewsAppDefaultProgressIndicator
import com.rbrauwers.newsapp.resources.MultiplatformResources
import dev.icerock.moko.resources.compose.stringResource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import openUrl

object HeadlineTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(MultiplatformResources.strings.headlines)
            val icon = rememberVectorPainter(Icons.Filled.List)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        HeadlineScreen().Content()
    }
}

private class HeadlineScreen : Screen {

    @OptIn(ExperimentalVoyagerApi::class)
    @Composable
    override fun Content() {
        //val screenModel = rememberScreenModel { HeadlineScreenModel() }
        //val screenModel = getScreenModel<HeadlineScreenModel>()

        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.getNavigatorScreenModel<HeadlineScreenModel>()

        val uiState: HeadlineUiState by screenModel.headlineUiState.collectAsState()
        HeadlineScreenContent(uiState = uiState)
    }

}

@Composable
private fun HeadlineScreenContent(
    uiState: HeadlineUiState
) {
    when (uiState) {
        is HeadlineUiState.Error -> {
            // TODO
        }

        is HeadlineUiState.Loading -> {
            NewsAppDefaultProgressIndicator(placeOnCenter = true)
        }

        is HeadlineUiState.Success -> {
            HeadlinesList(uiState = uiState)
        }
    }

}

@Composable
private fun HeadlinesList(
    listState: LazyListState = rememberLazyListState(),
    uiState: HeadlineUiState.Success
) {
    LazyColumn(
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        headlines(headlines = uiState.headlines)
    }
}

private fun LazyListScope.headlines(headlines: List<ArticleUi>) {
    itemsIndexed(
        items = headlines,
        key = { _, article -> article.id }
    ) { index, article ->
        Headline(article = article, isFirst = index == 0, isLast = index == headlines.lastIndex)
    }
}

@Composable
internal fun Headline(
    article: ArticleUi,
    isFirst: Boolean = false,
    isLast: Boolean = false
) {
    Card(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .padding(
                top = if (isFirst) 12.dp else 4.dp,
                bottom = if (isLast) 12.dp else 4.dp,
                start = 16.dp,
                end = 16.dp
            )
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

                article.publishedAt?.let { date ->
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.onPrimary,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(4.dp)
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
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                        )
                    }
                }

                FilledIconButton(
                    onClick = {
                        openUrl(url = article.url)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(imageVector = Icons.Outlined.OpenInBrowser, contentDescription = "")
                }
            }
        }


        /*
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
        ) {
            val (author, title, date, image, web) = createRefs()
            val context = LocalContext.current

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(article.urlToImage)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .constrainAs(image) {
                        end.linkTo(parent.end)
                    }
            )

            Text(
                text = article.author.orEmpty(),
                maxLines = 1,
                style = MaterialTheme.typography.titleSmall,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(author) {
                    width = Dimension.fillToConstraints
                    start.linkTo(parent.start)
                    end.linkTo(image.start, margin = 16.dp)
                }
            )

            Text(
                text = article.title.orEmpty(),
                maxLines = 4,
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(title) {
                        width = Dimension.fillToConstraints
                        top.linkTo(author.bottom, margin = 2.dp)
                        start.linkTo(author.start)
                        end.linkTo(author.end)
                    }
            )

            Text(
                text = article.publishedAt.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .constrainAs(date) {
                        bottom.linkTo(web.bottom, margin = 4.dp)
                        start.linkTo(author.start)
                    }
                    .background(
                        MaterialTheme.colorScheme.onPrimary,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(4.dp)
            )

            FilledIconButton(
                onClick = {
                    article.openUrl(context = context)
                },
                modifier = Modifier
                    .constrainAs(web) {
                        end.linkTo(image.end)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    }
            ) {
                Icon(imageVector = Icons.Outlined.OpenInBrowser, contentDescription = "")
            }
        }
         */
    }
}
