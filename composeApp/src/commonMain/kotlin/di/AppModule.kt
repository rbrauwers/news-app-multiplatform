package di

import com.rbrauwers.newsapp.auth.di.authModule
import com.rbrauwers.newsapp.designsystem.di.designSystemModule
import com.rbrauwers.newsapp.headlines.di.headlineModule
import com.rbrauwers.newsapp.network.di.networkModule
import com.rbrauwers.newsapp.sources.di.sourceModule
import org.koin.dsl.bind
import org.koin.dsl.module
import settings.DefaultSettingsComponent
import settings.SettingsComponent

internal val appModule = module {
    includes(networkModule, headlineModule, sourceModule, authModule, designSystemModule)

    factory { params ->
        DefaultSettingsComponent(
            componentContext = params.get(),
            mainContext = params.get(),
            headlineRepository = get(),
            icerockBiometricAuthenticator = get(),
            propertyBiometricAuthenticator = get(),
            platformInsetProvider = get()
        )
    } bind SettingsComponent::class
}