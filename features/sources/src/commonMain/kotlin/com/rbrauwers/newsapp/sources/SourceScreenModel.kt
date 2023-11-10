package com.rbrauwers.newsapp.sources

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.rbrauwers.newsapp.common.Result
import com.rbrauwers.newsapp.common.asResult
import com.rbrauwers.newsapp.data.repository.SourceRepository
import com.rbrauwers.newsapp.model.NewsSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class SourceScreenModel(
    private val sourceRepository: SourceRepository
) : ScreenModel {

    val sourceUiState: StateFlow<SourceUiState> =
        produceSourceUiState()
            .stateIn(
                scope = screenModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = SourceUiState.Loading
            )

    init {
        screenModelScope.launch {
            sourceRepository.sync()
        }
    }

    private fun produceSourceUiState(): Flow<SourceUiState> {
        return sourceRepository
            .getSources()
            .filterNotNull()
            .asResult()
            .map { it.toSourceUiState() }
    }

}

internal sealed interface SourceUiState {
    data class Success(val sources: List<NewsSource>) : SourceUiState
    data object Error : SourceUiState
    data object Loading : SourceUiState
}

private fun Result<List<NewsSource>>.toSourceUiState(): SourceUiState {
    return when (this) {
        is Result.Loading -> SourceUiState.Loading
        is Result.Error -> SourceUiState.Error
        is Result.Success -> SourceUiState.Success(data)
    }
}