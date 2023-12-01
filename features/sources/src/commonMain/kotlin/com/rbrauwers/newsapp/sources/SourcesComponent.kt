package com.rbrauwers.newsapp.sources

import com.arkivanov.decompose.ComponentContext
import com.rbrauwers.newsapp.common.Result
import com.rbrauwers.newsapp.common.asResult
import com.rbrauwers.newsapp.common.coroutineScope
import com.rbrauwers.newsapp.data.repository.SourceRepository
import com.rbrauwers.newsapp.model.NewsSource
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

interface SourcesComponent {
    val sourceUiState: StateFlow<SourceUiState>
}

internal class DefaultSourcesComponent(
    componentContext: ComponentContext,
    mainContext: CoroutineContext,
    private val sourceRepository: SourceRepository
) : SourcesComponent, ComponentContext by componentContext {

    // The scope is automatically cancelled when the component is destroyed
    private val scope = coroutineScope(mainContext + SupervisorJob())

    override val sourceUiState: StateFlow<SourceUiState> =
        produceSourceUiState()
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = SourceUiState.Loading
            )

    init {
        scope.launch {
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

sealed interface SourceUiState {
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