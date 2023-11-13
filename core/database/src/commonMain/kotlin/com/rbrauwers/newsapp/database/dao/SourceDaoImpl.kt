package com.rbrauwers.newsapp.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.rbrauwers.newsapp.database.NewsSourceEntity
import com.rbrauwers.newsapp.database.NewsSourceEntityQueries
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

internal class SourceDaoImpl(
    private val queries: NewsSourceEntityQueries,
    private val context: CoroutineContext
) : SourceDao {

    override fun getSources(): Flow<List<NewsSourceEntity>> {
        return queries.selectAll().asFlow().mapToList(context)
    }

    override suspend fun upsertSources(sources: List<NewsSourceEntity>) {
        queries.transaction {
            sources.forEach { item ->
                queries.upsert(
                    name = item.name,
                    description = item.description,
                    url = item.url,
                    category = item.category,
                    language = item.language,
                    country = item.country,
                    id = item.id
                )
            }
        }
    }

}