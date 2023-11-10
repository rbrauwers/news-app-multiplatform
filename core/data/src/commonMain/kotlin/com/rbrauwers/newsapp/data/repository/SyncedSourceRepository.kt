package com.rbrauwers.newsapp.data.repository

import com.rbrauwers.newsapp.model.NewsSource
import com.rbrauwers.newsapp.network.NetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SyncedSourceRepository(
    private val networkDataSource: NetworkDataSource
) : SourceRepository {

    private val _sourceFlow = MutableStateFlow<List<NewsSource>?>(null)
    private val sourceFlow = _sourceFlow.asStateFlow()

    override fun getSources(): Flow<List<NewsSource>?> {
        return sourceFlow
    }

    override suspend fun sync() {
        runCatching {
            networkDataSource.getSources()
        }.onSuccess {
            _sourceFlow.emit(it.sources)
        }.onFailure {
            println("SyncedSourceRepository::sync failure $it")
        }
    }

}