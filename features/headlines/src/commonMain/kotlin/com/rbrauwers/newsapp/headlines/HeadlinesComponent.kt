package com.rbrauwers.newsapp.headlines

import androidx.compose.runtime.Immutable
import com.arkivanov.decompose.ComponentContext
import com.rbrauwers.newsapp.common.Result
import com.rbrauwers.newsapp.common.asResult
import com.rbrauwers.newsapp.common.converters.ConvertStringToFormattedDate
import com.rbrauwers.newsapp.common.coroutineScope
import com.rbrauwers.newsapp.data.repository.HeadlineRepository
import com.rbrauwers.newsapp.model.Article
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

interface HeadlinesComponent {
    val headlineUiState: StateFlow<HeadlineUiState>

    fun updateLiked(article: ArticleUi, value: Boolean)
}

internal class DefaultHeadlinesComponent(
    componentContext: ComponentContext,
    mainContext: CoroutineContext,
    private val headlineRepository: HeadlineRepository
) : HeadlinesComponent, ComponentContext by componentContext {

    // The scope is automatically cancelled when the component is destroyed
    private val scope = coroutineScope(mainContext + SupervisorJob())

    override val headlineUiState: StateFlow<HeadlineUiState> =
        produceHeadlineUiState()
            .flowOn(Dispatchers.IO)
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = HeadlineUiState.Loading
            )

    init {
        scope.launch {
            headlineRepository.sync()
        }
    }

    override fun updateLiked(article: ArticleUi, value: Boolean) {
        scope.launch {
            headlineRepository
                .updateLiked(id = article.id.toLong(), value = value)
        }
    }

    private fun produceHeadlineUiState(): Flow<HeadlineUiState> {
        return headlineRepository
            .getHeadlines()
            .filterNotNull()
            .asResult()
            .map { it.toHeadlineUiState() }
    }

}

@Immutable
sealed interface HeadlineUiState {
    data class Success(val headlines: ImmutableList<ArticleUi>) : HeadlineUiState
    data object Error : HeadlineUiState
    data object Loading : HeadlineUiState
}

private fun Result<List<Article>>.toHeadlineUiState(): HeadlineUiState {
    return when (this) {
        is Result.Loading -> HeadlineUiState.Loading
        is Result.Error -> HeadlineUiState.Error
        is Result.Success -> {
            val converter = ConvertStringToFormattedDate()
            HeadlineUiState.Success(
                data.map { it.toArticleUi(converter) }
                    .toPersistentList()
            )
        }
    }
}

private fun Article.toArticleUi(dateConverter: ConvertStringToFormattedDate) = ArticleUi(
    id = id,
    author = if (author.isNullOrBlank()) "Author: N/A" else author,
    title = title,
    urlToImage = urlToImage,
    url = url,
    publishedAt = publishedAt,
    liked = liked,
    dateConverter = dateConverter
)

@Immutable
data class ArticleUi(
    val id: Int,
    val author: String?,
    val title: String?,
    val urlToImage: String?,
    val url: String?,
    val publishedAt: String?,
    val liked: Boolean,
    val dateConverter: ConvertStringToFormattedDate
) {

    val formattedPublishedAt: String? by lazy {
        dateConverter(publishedAt)
    }

}