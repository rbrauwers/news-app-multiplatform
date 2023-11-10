package com.rbrauwers.newsapp.data.di

import com.rbrauwers.newsapp.data.repository.HeadlineRepository
import com.rbrauwers.newsapp.data.repository.SourceRepository
import com.rbrauwers.newsapp.data.repository.SyncedHeadlineRepository
import com.rbrauwers.newsapp.data.repository.SyncedSourceRepository
import com.rbrauwers.newsapp.network.di.networkModule
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    includes(networkModule)

    single {
        SyncedHeadlineRepository(networkDataSource = get())
    } bind HeadlineRepository::class

    single {
        SyncedSourceRepository(networkDataSource = get())
    } bind SourceRepository::class
}