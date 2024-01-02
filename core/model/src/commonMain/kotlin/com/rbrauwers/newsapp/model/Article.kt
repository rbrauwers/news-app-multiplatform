package com.rbrauwers.newsapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?,
    val liked: Boolean = false,
    // Artificial ID since it is missing on API
    val id: Int = (author.orEmpty() + title.orEmpty() + description.orEmpty() + url.orEmpty() + urlToImage.orEmpty() + publishedAt.orEmpty() + content.orEmpty()).hashCode()
)