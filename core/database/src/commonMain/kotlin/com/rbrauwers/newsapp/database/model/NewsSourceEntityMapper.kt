package com.rbrauwers.newsapp.database.model

import com.rbrauwers.newsapp.database.NewsSourceEntity
import com.rbrauwers.newsapp.model.NewsSource

fun NewsSourceEntity.toExternalModel() = NewsSource(
    id = id,
    name = name,
    description = description,
    url = url,
    category = category,
    language = language,
    country = country
)