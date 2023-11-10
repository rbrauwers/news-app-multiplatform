package com.rbrauwers.newsapp.sources.di

import com.rbrauwers.newsapp.data.di.dataModule
import com.rbrauwers.newsapp.sources.SourceScreenModel
import org.koin.dsl.module

val sourceModule = module {
    includes(dataModule)

    factory {
        SourceScreenModel(sourceRepository = get())
    }
}