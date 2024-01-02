package com.rbrauwers.newsapp.sources

import com.rbrauwers.newsapp.model.NewsSource

interface SourceDetailComponent {
    val source: NewsSource
}

internal class DefaultSourceDetailComponent(
    override val source: NewsSource
) : SourceDetailComponent