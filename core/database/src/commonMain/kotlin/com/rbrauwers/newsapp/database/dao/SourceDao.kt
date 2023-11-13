package com.rbrauwers.newsapp.database.dao

import com.rbrauwers.newsapp.database.NewsSourceEntity
import kotlinx.coroutines.flow.Flow

interface SourceDao {

    fun getSources(): Flow<List<NewsSourceEntity>>

    suspend fun upsertSources(sources: List<NewsSourceEntity>)

}