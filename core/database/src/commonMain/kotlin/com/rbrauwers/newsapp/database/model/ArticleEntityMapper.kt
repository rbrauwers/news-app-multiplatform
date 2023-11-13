package com.rbrauwers.newsapp.database.model

import com.rbrauwers.newsapp.database.ArticleEntity
import com.rbrauwers.newsapp.model.Article

fun ArticleEntity.toExternalModel() = Article(
    id = id.toInt(),
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content
)