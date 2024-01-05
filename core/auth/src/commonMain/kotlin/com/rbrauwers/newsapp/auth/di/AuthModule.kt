package com.rbrauwers.newsapp.auth.di

import com.rbrauwers.newsapp.auth.DefaultIcerockBiometricAuthenticator
import com.rbrauwers.newsapp.auth.IcerockBiometricAuthenticator
import com.rbrauwers.newsapp.auth.PlatformBiometricAuthenticator
import com.rbrauwers.newsapp.auth.PropertyBiometricAuthenticator
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {

    single {
        DefaultIcerockBiometricAuthenticator()
    } bind IcerockBiometricAuthenticator::class


    single {
        PlatformBiometricAuthenticator()
    } bind PropertyBiometricAuthenticator::class

}