package com.rbrauwers.newsapp.sources.di

import com.rbrauwers.newsapp.data.di.dataModule
import com.rbrauwers.newsapp.sources.DefaultSourceDetailComponent
import com.rbrauwers.newsapp.sources.DefaultSourcesListComponent
import com.rbrauwers.newsapp.sources.DefaultSourcesComponent
import com.rbrauwers.newsapp.sources.SourceDetailComponent
import com.rbrauwers.newsapp.sources.SourcesListComponent
import com.rbrauwers.newsapp.sources.SourcesComponent
import org.koin.dsl.bind
import org.koin.dsl.module

val sourceModule = module {
    includes(dataModule)

    factory { params ->
        DefaultSourcesComponent(
            componentContext = params.get(),
            onNavigateToInfo = params.get(),
            onNavigateToSettings = params.get()
        )
    } bind SourcesComponent::class

    factory { params ->
        DefaultSourcesListComponent(
            componentContext = params.get(),
            mainContext = params.get(),
            sourceRepository = get()
        )
    } bind SourcesListComponent::class

    factory { params ->
        DefaultSourceDetailComponent(source = params.get())
    } bind SourceDetailComponent::class

}