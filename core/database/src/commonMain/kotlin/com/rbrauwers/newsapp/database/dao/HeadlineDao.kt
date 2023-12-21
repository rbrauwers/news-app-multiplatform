package com.rbrauwers.newsapp.database.dao

import com.rbrauwers.newsapp.database.ArticleEntity
import kotlinx.coroutines.flow.Flow

interface HeadlineDao {

    fun getHeadlines(): Flow<List<ArticleEntity>>

    suspend fun upsertHeadlines(headlines: List<ArticleEntity>)

    suspend fun updateLiked(id: Long, value: Boolean)

}