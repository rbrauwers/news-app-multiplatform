package components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.value.Value
import com.rbrauwers.newsapp.headlines.HeadlinesComponent
import com.rbrauwers.newsapp.sources.SourcesComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

interface RootComponent {
    val stack: Value<ChildStack<*, NewsAppChild>>

    fun onBackClicked()
    fun onHeadlinesTabClicked()
    fun onSourcesTabClicked()
    fun onInfoClicked()

    sealed class NewsAppChild {
        class Headlines(val component: HeadlinesComponent) : NewsAppChild()
        class Sources(val component: SourcesComponent) : NewsAppChild()
        data object Info : NewsAppChild()
    }
}

class AppRootComponent(
    componentContext: ComponentContext
) : RootComponent, KoinComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.NewsAppChild>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Headlines,
            handleBackButton = true,
            childFactory = ::child,
        )

    override fun onBackClicked() {
        navigation.pop()
    }

    override fun onHeadlinesTabClicked() {
        navigation.bringToFront(Config.Headlines)
    }

    override fun onSourcesTabClicked() {
        navigation.bringToFront(Config.Sources)
    }

    override fun onInfoClicked() {
        navigation.bringToFront(Config.Info)
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
        parametersOf(componentContext, Dispatchers.Main)
    })
}