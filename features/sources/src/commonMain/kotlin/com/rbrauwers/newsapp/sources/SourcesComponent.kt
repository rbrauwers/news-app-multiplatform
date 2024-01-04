package com.rbrauwers.newsapp.sources

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.rbrauwers.newsapp.model.NewsSource
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

interface SourcesComponent {
    val stack: Value<ChildStack<*, SourcesChild>>
    val onNavigateToInfo: () -> Unit
    val onNavigateToSettings: () -> Unit

    fun onNavigateBack()
    fun onNavigateToSource(source: NewsSource)

    sealed class SourcesChild {
        data class SourcesList(val component: SourcesListComponent) : SourcesChild()
        data class SourceDetail(val component: SourceDetailComponent) : SourcesChild()
    }
}

internal class DefaultSourcesComponent(
    componentContext: ComponentContext,
    override val onNavigateToInfo: () -> Unit,
    override val onNavigateToSettings: () -> Unit
) : SourcesComponent, KoinComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, SourcesComponent.SourcesChild>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.SourcesList,
            handleBackButton = true,
            childFactory = ::child,
        )

    override fun onNavigateBack() {
        navigation.pop()
    }

    override fun onNavigateToSource(source: NewsSource) {
        navigation.bringToFront(Config.SourceDetail(source = source))
    }

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): SourcesComponent.SourcesChild {
        return when (config) {
            is Config.SourcesList -> {
                SourcesComponent.SourcesChild.SourcesList(
                    component = sourcesList(
                        componentContext = componentContext
                    )
                )
            }

            is Config.SourceDetail -> {
                SourcesComponent.SourcesChild.SourceDetail(
                    component = sourceDetail(
                        componentContext = componentContext,
                        source = config.source
                    )
                )
            }
        }
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object SourcesList : Config

        @Serializable
        data class SourceDetail(val source: NewsSource) : Config
    }

}

private fun DefaultSourcesComponent.sourcesList(
    componentContext: ComponentContext
): SourcesListComponent {
    return get(parameters = {
        parametersOf(componentContext, Dispatchers.Main)
    })
}

private fun DefaultSourcesComponent.sourceDetail(
    componentContext: ComponentContext,
    source: NewsSource
): SourceDetailComponent {
    return get(parameters = {
        parametersOf(componentContext, source)
    })
}
