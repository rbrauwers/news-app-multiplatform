import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.rbrauwers.newsapp.designsystem.AppState
import com.rbrauwers.newsapp.designsystem.LocalAppState
import com.rbrauwers.newsapp.designsystem.TopBarState
import com.rbrauwers.newsapp.designsystem.theme.NewsAppTheme
import com.rbrauwers.newsapp.headlines.HeadlineScreen
import com.rbrauwers.newsapp.resources.MultiplatformResources
import com.rbrauwers.newsapp.sources.SourcesChildren
import components.RootComponent
import dev.icerock.moko.resources.compose.stringResource
import info.InfoScreen
import settings.SettingsScreen

@Composable
fun App(component: RootComponent, modifier: Modifier = Modifier) {
    val childStack by component.stack.subscribeAsState()
    val activeChild = childStack.active.instance

    CompositionLocalProvider(LocalAppState provides AppState()) {
        NewsAppTheme {
            Scaffold(
                topBar = {
                    NewsAppTopBar()
                },
                bottomBar = {
                    NewsAppBottomBar(
                        component = component,
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewsAppTopBar() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val topBarState: TopBarState by LocalAppState.current.topBarStateFlow.collectAsState()

    CenterAlignedTopAppBar(
        title = topBarState.title ?: { },
        scrollBehavior = scrollBehavior,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        actions = topBarState.actions ?: { },
        navigationIcon = topBarState.navigationIcon ?: { },
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
                HeadlineScreen(
                    component = instance.component,
                    onNavigateToInfo = component::onNavigateToInfo,
                    onNavigateToSettings = component::onNavigateToSettings
                )
            }

            is RootComponent.NewsAppChild.Sources -> {
                SourcesChildren(component = instance.component)
            }

            is RootComponent.NewsAppChild.Info -> {
                InfoScreen(onNavigateBack = component::onNavigateBack)
            }

            is RootComponent.NewsAppChild.Settings -> {
                SettingsScreen(
                    component = instance.component,
                    onNavigateBack = component::onNavigateBack
                )
            }
        }
    }
}

@Composable
private fun NewsAppBottomBar(
    component: RootComponent,
    activeChild: RootComponent.NewsAppChild,
    modifier: Modifier = Modifier
) {
    val bottomBarState = LocalAppState.current.bottomBarStateFlow.collectAsState()

    AnimatedVisibility(
        visible = bottomBarState.value.isVisible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { fullHeight -> fullHeight/2 }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { fullHeight -> fullHeight/2 })
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
    NewsBarItem(uiState = uiState, onClick = component::onNavigateToHeadlines)
}

@Composable
private fun RowScope.SourcesBarItem(component: RootComponent, activeChild: RootComponent.NewsAppChild) {
    val uiState = ChildUIState.Sources(isSelected = activeChild is RootComponent.NewsAppChild.Sources)
    NewsBarItem(uiState = uiState, onClick = component::onNavigateToSources)
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
}