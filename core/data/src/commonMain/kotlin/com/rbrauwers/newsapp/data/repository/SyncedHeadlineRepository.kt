package com.rbrauwers.newsapp.data.repository

import com.rbrauwers.newsapp.model.Article
import com.rbrauwers.newsapp.network.NetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SyncedHeadlineRepository(
    private val networkDataSource: NetworkDataSource
) : HeadlineRepository {

    private val _headlineFlow = MutableStateFlow<List<Article>?>(null)
    private val headlineFlow = _headlineFlow.asStateFlow()

    override fun getHeadlines(): Flow<List<Article>?> {
        return headlineFlow
    }

    override suspend fun sync() {
        runCatching {
            networkDataSource.getHeadlines()
        }.onSuccess {
            _headlineFlow.emit(it.articles)
        }.onFailure {
            println("SyncedHeadlineRepository::sync failure $it")
        }
    }

}