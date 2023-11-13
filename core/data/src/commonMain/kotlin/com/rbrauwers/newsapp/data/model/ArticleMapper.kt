package com.rbrauwers.newsapp.data.model

import com.rbrauwers.newsapp.database.ArticleEntity
import com.rbrauwers.newsapp.model.Article

fun Article.toEntity() = ArticleEntity(
    id = id.toLong(),
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content
)