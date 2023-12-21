package com.rbrauwers.newsapp.data.repository

import com.rbrauwers.newsapp.common.isOk
import com.rbrauwers.newsapp.data.model.toEntity
import com.rbrauwers.newsapp.database.dao.HeadlineDao
import com.rbrauwers.newsapp.database.model.toExternalModel
import com.rbrauwers.newsapp.model.Article
import com.rbrauwers.newsapp.network.NetworkDataSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SyncedHeadlineRepository(
    private val networkDataSource: NetworkDataSource,
    private val dao: HeadlineDao
) : HeadlineRepository {

    override fun getHeadlines(): Flow<List<Article>?> {
        return dao.getHeadlines().map { it.map { article -> article.toExternalModel() } }
    }

    override suspend fun sync() {
        runCatching {
            val response = networkDataSource.getHeadlines()

            // Saves data in local store regardless if coroutine context was cancelled
            withContext(NonCancellable) {
                if (response.status.isOk()) {
                    dao.upsertHeadlines(response.articles.map { it.toEntity() })
                }
            }
        }.onFailure {
            // Do not suppress coroutine cancellations
            if (it is CancellationException) throw it
            println("SyncedHeadlineRepository::sync failure $it")
        }
    }

}