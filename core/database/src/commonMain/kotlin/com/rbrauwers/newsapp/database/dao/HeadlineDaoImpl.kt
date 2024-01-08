package com.rbrauwers.newsapp.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.rbrauwers.newsapp.database.ArticleEntity
import com.rbrauwers.newsapp.database.ArticleEntityQueries
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

internal class HeadlineDaoImpl(
    private val queries: ArticleEntityQueries,
    private val context: CoroutineContext
) : HeadlineDao {

    override fun getHeadlines(): Flow<List<ArticleEntity>> {
        return queries.selectAll()
            .asFlow()
            .mapToList(context)
    }

    override fun upsertHeadlines(headlines: List<ArticleEntity>) {
        queries.transaction {
            headlines.forEach { item ->
                queries.upsert(
                    id = item.id,
                    author = item.author,
                    title = item.title,
                    url = item.url,
                    urlToImage = item.urlToImage,
                    description = item.description,
                    publishedAt = item.publishedAt,
                    content = item.content
                )
            }
        }
    }

    override fun updateLiked(id: Long, value: Boolean) {
        queries.updateLiked(id = id, liked = if (value) 1L else 0L)
    }

    override fun updateLikes(likes: Map<Long, Boolean>) {
        queries.transaction {
            likes.forEach { (id, liked) ->
                updateLiked(id = id, value = liked)
            }
        }
    }

}