package com.rbrauwers.newsapp.headlines.di

import com.rbrauwers.newsapp.data.di.dataModule
import com.rbrauwers.newsapp.headlines.DefaultHeadlinesComponent
import com.rbrauwers.newsapp.headlines.HeadlinesComponent
import org.koin.dsl.bind
import org.koin.dsl.module

val headlineModule = module {
    includes(dataModule)

    factory { params ->
        DefaultHeadlinesComponent(
            componentContext = params.get(),
            mainContext = params.get(),
            headlineRepository = get()
        )
    } bind HeadlinesComponent::class

}