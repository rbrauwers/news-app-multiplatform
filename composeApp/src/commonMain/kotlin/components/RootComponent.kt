package components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.rbrauwers.newsapp.designsystem.providers.PlatformInsetProvider
import com.rbrauwers.newsapp.headlines.HeadlinesComponent
import com.rbrauwers.newsapp.sources.SourcesComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import settings.SettingsComponent

interface RootComponent {
    val stack: Value<ChildStack<*, NewsAppChild>>
    val platformInsetProvider: PlatformInsetProvider

    fun onNavigateBack()
    fun onNavigateToHeadlines()
    fun onNavigateToSources()
    fun onNavigateToInfo()
    fun onNavigateToSettings()

    sealed class NewsAppChild {
        class Headlines(val component: HeadlinesComponent) : NewsAppChild()
        class Sources(val component: SourcesComponent) : NewsAppChild()
        internal class Settings(val component: SettingsComponent) : NewsAppChild()
        internal data object Info : NewsAppChild()
    }
}

internal class AppRootComponent(
    componentContext: ComponentContext
) : RootComponent, KoinComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()
    override val platformInsetProvider: PlatformInsetProvider = get()

    override val stack: Value<ChildStack<*, RootComponent.NewsAppChild>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Headlines,
            handleBackButton = true,
            childFactory = ::child
        )

    override fun onNavigateBack() {
        navigation.pop()
    }

    override fun onNavigateToHeadlines() {
        navigation.bringToFront(Config.Headlines)
    }

    override fun onNavigateToSources() {
        navigation.bringToFront(Config.Sources)
    }

    override fun onNavigateToInfo() {
        navigation.bringToFront(Config.Info)
    }

    override fun onNavigateToSettings() {
        navigation.bringToFront(Config.Settings)
    }

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.NewsAppChild {
        return when (config) {
            is Config.Headlines -> {
                RootComponent.NewsAppChild.Headlines(headlinesComponent(componentContext = componentContext))
            }

            is Config.Sources -> {
                RootComponent.NewsAppChild.Sources(sourcesComponent(componentContext = componentContext))
            }

            is Config.Info -> {
                RootComponent.NewsAppChild.Info
            }

            is Config.Settings -> {
                RootComponent.NewsAppChild.Settings(settingsComponent(componentContext = componentContext))
            }
        }
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Headlines : Config

        @Serializable
        data object Sources : Config

        @Serializable
        data object Info : Config

        @Serializable
        data object Settings : Config
    }
}

private fun AppRootComponent.headlinesComponent(
    componentContext: ComponentContext
): HeadlinesComponent {
    return get(parameters = {
        parametersOf(componentContext, Dispatchers.Main)
    })
}

private fun AppRootComponent.sourcesComponent(
    componentContext: ComponentContext
): SourcesComponent {
    return get(parameters = {
        parametersOf(componentContext, this::onNavigateToInfo, this::onNavigateToSettings)
    })
}

private fun AppRootComponent.settingsComponent(
    componentContext: ComponentContext
): SettingsComponent {
    return get(parameters = {
        parametersOf(componentContext, Dispatchers.Main)
    })
}