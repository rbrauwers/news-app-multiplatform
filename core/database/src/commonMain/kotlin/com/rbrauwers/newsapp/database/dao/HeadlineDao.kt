package com.rbrauwers.newsapp.database.dao

import com.rbrauwers.newsapp.database.ArticleEntity
import kotlinx.coroutines.flow.Flow

interface HeadlineDao {

    fun getHeadlines(): Flow<List<ArticleEntity>>

    fun upsertHeadlines(headlines: List<ArticleEntity>)

    fun updateLiked(id: Long, value: Boolean)

    fun updateLikes(likes: Map<Long, Boolean>)

}