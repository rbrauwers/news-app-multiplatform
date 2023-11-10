package com.rbrauwers.newsapp.headlines

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.rbrauwers.newsapp.common.Result
import com.rbrauwers.newsapp.common.asResult
import com.rbrauwers.newsapp.common.converters.ConvertStringToDateTimeInstance
import com.rbrauwers.newsapp.data.repository.HeadlineRepository
import com.rbrauwers.newsapp.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class HeadlineScreenModel(
    private val headlineRepository: HeadlineRepository
) : ScreenModel {

    val headlineUiState: StateFlow<HeadlineUiState> =
        produceHeadlineUiState()
            .stateIn(
                scope = screenModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = HeadlineUiState.Loading
            )

    init {
        screenModelScope.launch {
            headlineRepository.sync()
        }
    }

    private fun produceHeadlineUiState(): Flow<HeadlineUiState> {
        return headlineRepository
            .getHeadlines()
            .asResult()
            .map { it.toHeadlineUiState() }
    }
}

internal sealed interface HeadlineUiState {
    data class Success(val headlines: List<ArticleUi>) : HeadlineUiState
    data object Error : HeadlineUiState
    data object Loading : HeadlineUiState
}

private fun Result<List<Article>>.toHeadlineUiState(): HeadlineUiState {
    return when (this) {
        is Result.Loading -> HeadlineUiState.Loading
        is Result.Error -> HeadlineUiState.Error
        is Result.Success -> {
            val converter = ConvertStringToDateTimeInstance()
            HeadlineUiState.Success(data.map { it.toArticleUi(converter) })
        }
    }
}

private fun Article.toArticleUi(dateConverter: ConvertStringToDateTimeInstance) = ArticleUi(
    id = id,
    author = if (author.isNullOrBlank()) "Author: N/A" else author,
    title = title,
    urlToImage = urlToImage,
    url = url,
    publishedAt = dateConverter(publishedAt)
)

internal data class ArticleUi(
    val id: Int,
    val author: String?,
    val title: String?,
    val urlToImage: String?,
    val url: String?,
    val publishedAt: String?
)