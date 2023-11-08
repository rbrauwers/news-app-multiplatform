package com.rbrauwers.newsapp.network.di

import com.rbrauwers.newsapp.network.NetworkDataSource
import com.rbrauwers.newsapp.network.NewsHttpClient
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {

    single {
        NewsHttpClient()
    } bind NetworkDataSource::class

}