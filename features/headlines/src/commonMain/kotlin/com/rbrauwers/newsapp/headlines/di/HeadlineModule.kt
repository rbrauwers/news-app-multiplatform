package com.rbrauwers.newsapp.headlines.di

import com.rbrauwers.newsapp.data.di.dataModule
import com.rbrauwers.newsapp.headlines.HeadlineScreenModel
import org.koin.dsl.module

val headlineModule = module {
    includes(dataModule)

    factory {
        HeadlineScreenModel(headlineRepository = get())
    }
}