package com.rbrauwers.newsapp.database.di

import com.rbrauwers.newsapp.database.DatabaseDriverFactory
import com.rbrauwers.newsapp.database.NewsMultiplatformDatabase
import com.rbrauwers.newsapp.database.dao.HeadlineDao
import com.rbrauwers.newsapp.database.dao.HeadlineDaoImpl
import com.rbrauwers.newsapp.database.dao.SourceDao
import com.rbrauwers.newsapp.database.dao.SourceDaoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {
    factoryOf(::DatabaseDriverFactory)

    single {
        NewsMultiplatformDatabase(driver = get<DatabaseDriverFactory>().createDriver())
    }

    single {
        get<NewsMultiplatformDatabase>().articleEntityQueries
    }

    single {
        get<NewsMultiplatformDatabase>().newsSourceEntityQueries
    }

    single {
        HeadlineDaoImpl(
            queries = get(),
            context = Dispatchers.IO
        )
    } bind HeadlineDao::class

    single {
        SourceDaoImpl(
            queries = get(),
            context = Dispatchers.IO
        )
    } bind SourceDao::class

}