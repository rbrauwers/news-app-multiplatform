package di

import com.rbrauwers.newsapp.headlines.di.headlineModule
import com.rbrauwers.newsapp.network.di.networkModule
import org.koin.dsl.module

internal val appModule = module {
    includes(networkModule, headlineModule)
}