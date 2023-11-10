package com.rbrauwers.newsapp.data.repository

import com.rbrauwers.newsapp.model.Article
import com.rbrauwers.newsapp.network.NetworkDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class SyncedHeadlineRepository(
    private val networkDataSource: NetworkDataSource
) : HeadlineRepository {

    private val channel = Channel<List<Article>>()
    private val headlineFlow = channel.receiveAsFlow()

    override fun getHeadlines(): Flow<List<Article>> {
        return headlineFlow
    }

    override suspend fun sync() {
        runCatching {
            networkDataSource.getHeadlines()
        }.onSuccess {
            channel.send(it.articles)
        }.onFailure {
            println("SyncedHeadlineRepository::sync failure $it")
        }
    }

}