package com.rbrauwers.newsapp.sources.di

import com.rbrauwers.newsapp.data.di.dataModule
import com.rbrauwers.newsapp.sources.DefaultSourcesComponent
import com.rbrauwers.newsapp.sources.SourcesComponent
import org.koin.dsl.bind
import org.koin.dsl.module

val sourceModule = module {
    includes(dataModule)

    factory { params ->
        DefaultSourcesComponent(
            componentContext = params.get(),
            mainContext = params.get(),
            sourceRepository = get()
        )
    } bind SourcesComponent::class
}