import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.rbrauwers.newsapp.designsystem.theme.NewsAppTheme
import com.rbrauwers.newsapp.headlines.HeadlineScreen
import com.rbrauwers.newsapp.resources.MultiplatformResources
import com.rbrauwers.newsapp.sources.SourceScreen
import components.RootComponent
import dev.icerock.moko.resources.compose.stringResource
import info.InfoScreen2

@Composable
fun App(component: RootComponent, modifier: Modifier = Modifier) {
    val childStack by component.stack.subscribeAsState()
    val childUiState = childStack.active.instance.uiState()
    val activeChild = childStack.active.instance

    NewsAppTheme {
        Scaffold(
            topBar = {
                NewsAppTopBar(
                    component = component,
                    childUiState = childUiState
                )
            },
            bottomBar = {
                NewsAppBottomBar(
                    component = component,
                    childUiState = childUiState,
                    activeChild = activeChild
                )
            },
            modifier = modifier
        ) {
            NewsAppChildren(
                component = component,
                modifier = Modifier.padding(it)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewsAppTopBar(
    component: RootComponent,
    childUiState: ChildUIState
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = childUiState.title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            if (childUiState.hasNavigationActions) {
                IconButton(
                    onClick = {
                        component.onInfoClicked()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null
                    )
                }
            }
        },
        navigationIcon = {
            if (childUiState.isBackButtonVisible) {
                IconButton(onClick = { component.onBackClicked() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    )
}

@Composable
private fun NewsAppChildren(component: RootComponent, modifier: Modifier) {
    Children(
        stack = component.stack,
        animation = stackAnimation(fade()),
        modifier = modifier
    ) { child ->
        when (val instance = child.instance) {
            is RootComponent.NewsAppChild.Headlines -> {
                HeadlineScreen(component = instance.component)
            }

            is RootComponent.NewsAppChild.Sources -> {
                SourceScreen(component = instance.component)
            }

            is RootComponent.NewsAppChild.Info -> {
                InfoScreen2()
            }
        }
    }
}

@Composable
private fun NewsAppBottomBar(
    component: RootComponent,
    childUiState: ChildUIState,
    activeChild: RootComponent.NewsAppChild,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = childUiState.isBottomBarVisible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { fullHeight ->  fullHeight/2 }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { fullHeight ->  fullHeight/2 })
    ) {
        NavigationBar(modifier = modifier) {
            HeadlinesBarItem(component = component, activeChild = activeChild)
            SourcesBarItem(component = component, activeChild = activeChild)
        }
    }
}

@Composable
private fun RowScope.HeadlinesBarItem(component: RootComponent, activeChild: RootComponent.NewsAppChild) {
    val uiState = ChildUIState.Headlines(isSelected = activeChild is RootComponent.NewsAppChild.Headlines)
    NewsBarItem(uiState = uiState, onClick = component::onHeadlinesTabClicked)
}

@Composable
private fun RowScope.SourcesBarItem(component: RootComponent, activeChild: RootComponent.NewsAppChild) {
    val uiState = ChildUIState.Sources(isSelected = activeChild is RootComponent.NewsAppChild.Sources)
    NewsBarItem(uiState = uiState, onClick = component::onSourcesTabClicked)
}

@Composable
private fun RowScope.NewsBarItem(uiState: ChildUIState, onClick: () -> Unit) {
    NavigationBarItem(
        selected = uiState.isSelected ?: false,
        onClick = onClick,
        icon = {
            uiState.icon?.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = uiState.title,
                )
            }
        },
        label = {
            Text(uiState.title)
        }
    )
}

private fun RootComponent.NewsAppChild.uiState(): ChildUIState {
    return when (this) {
        is RootComponent.NewsAppChild.Headlines -> {
            ChildUIState.Headlines()
        }

        is RootComponent.NewsAppChild.Sources -> {
            ChildUIState.Sources()
        }

        is RootComponent.NewsAppChild.Info -> {
            ChildUIState.Info()
        }
    }
}

private sealed interface ChildUIState {
    @get:Composable
    val title: String

    val icon: ImageVector?
    val isSelected: Boolean?
    val isBottomBarVisible: Boolean
    val hasNavigationActions: Boolean
    val isBackButtonVisible: Boolean

    data class Headlines(override val isSelected: Boolean? = null) : ChildUIState {
        override val title: String
            @Composable get() = stringResource(MultiplatformResources.strings.headlines)

        override val icon: ImageVector
            get() = Icons.Default.List

        override val isBottomBarVisible: Boolean = true
        override val hasNavigationActions: Boolean = true
        override val isBackButtonVisible: Boolean = false
    }

    data class Sources(override val isSelected: Boolean? = null) : ChildUIState {
        override val title: String
            @Composable get() = stringResource(MultiplatformResources.strings.sources)

        override val icon: ImageVector
            get() = Icons.Default.Person

        override val isBottomBarVisible: Boolean = true
        override val hasNavigationActions: Boolean = true
        override val isBackButtonVisible: Boolean = false
    }

    data class Info(override val isSelected: Boolean? = null) : ChildUIState {
        override val title: String
            @Composable get() = stringResource(MultiplatformResources.strings.app_info)

        override val icon: ImageVector? = null
        override val isBottomBarVisible: Boolean = false
        override val hasNavigationActions: Boolean = false
        override val isBackButtonVisible: Boolean = true
    }
}