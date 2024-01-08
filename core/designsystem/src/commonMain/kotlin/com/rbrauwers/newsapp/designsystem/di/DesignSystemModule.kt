package com.rbrauwers.newsapp.designsystem.di

import com.rbrauwers.newsapp.designsystem.PlatformInsetProviderImpl
import com.rbrauwers.newsapp.designsystem.providers.PlatformInsetProvider
import org.koin.dsl.bind
import org.koin.dsl.module

val designSystemModule = module {
    single {
        PlatformInsetProviderImpl()
    } bind PlatformInsetProvider::class
}