package com.rbrauwers.newsapp.sources

import androidx.compose.runtime.Immutable
import com.arkivanov.decompose.ComponentContext
import com.rbrauwers.newsapp.common.Result
import com.rbrauwers.newsapp.common.asResult
import com.rbrauwers.newsapp.common.coroutineScope
import com.rbrauwers.newsapp.data.repository.SourceRepository
import com.rbrauwers.newsapp.model.NewsSource
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
import org.koin.core.component.KoinComponent
import kotlin.coroutines.CoroutineContext

interface SourcesListComponent {
    val sourcesUiState: StateFlow<SourcesUiState>
}

internal class DefaultSourcesListComponent(
    componentContext: ComponentContext,
    mainContext: CoroutineContext,
    private val sourceRepository: SourceRepository
) : SourcesListComponent, KoinComponent, ComponentContext by componentContext {

    // The scope is automatically cancelled when the component is destroyed
    private val scope = coroutineScope(mainContext + SupervisorJob())

    override val sourcesUiState: StateFlow<SourcesUiState> =
        produceSourceUiState()
            .flowOn(Dispatchers.IO)
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = SourcesUiState.Loading
            )

    init {
        scope.launch {
            sourceRepository.sync()
        }
    }

    private fun produceSourceUiState(): Flow<SourcesUiState> {
        return sourceRepository
            .getSources()
            .filterNotNull()
            .asResult()
            .map { it.toSourceUiState() }
    }

}


@Immutable
sealed interface SourcesUiState {
    data class Success(val sources: ImmutableList<NewsSource>) : SourcesUiState
    data object Error : SourcesUiState
    data object Loading : SourcesUiState
}

private fun Result<List<NewsSource>>.toSourceUiState(): SourcesUiState {
    return when (this) {
        is Result.Loading -> SourcesUiState.Loading
        is Result.Error -> SourcesUiState.Error
        is Result.Success -> SourcesUiState.Success(data.toPersistentList())
    }
}