package com.rbrauwers.newsapp.auth.di

import com.rbrauwers.newsapp.auth.BiometricAuthenticator
import com.rbrauwers.newsapp.auth.DefaultBiometricAuthenticator
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlinx.coroutines.Dispatchers

val authModule = module {
    single {
        DefaultBiometricAuthenticator(
            mainContext = Dispatchers.Main
        )
    } bind BiometricAuthenticator::class
}