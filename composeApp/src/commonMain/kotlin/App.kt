import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.rbrauwers.newsapp.designsystem.theme.NewsAppTheme
import com.rbrauwers.newsapp.headlines.HeadlineTab
import com.rbrauwers.newsapp.sources.SourceTab
import info.InfoScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    NewsAppTheme {
        val (appState, setAppState) = remember {
            mutableStateOf(AppState())
        }

        Navigator(TabsScreen(onAppStateChanged = setAppState)) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = appState.title,
                                style = MaterialTheme.typography.titleLarge
                            )
                        },
                        scrollBehavior = scrollBehavior,
                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                        actions = {
                            val navigator = LocalNavigator.currentOrThrow
                            if (appState.isNavigationActionsEnabled) {
                                IconButton(
                                    onClick = {
                                        navigator.push(InfoScreen(onAppStateChanged = setAppState))
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
                            val navigator = LocalNavigator.currentOrThrow
                            if (appState.isNavigationIconEnabled) {
                                IconButton(onClick = { navigator.pop() }) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    )
                },
                content = {
                    Surface(modifier = Modifier.padding(it)) {
                        CurrentScreen()
                    }
                }
            )
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(
    tab: Tab
) {
    val tabNavigator = LocalTabNavigator.current

    NavigationBarItem(
        selected = tabNavigator.current.key == tab.key,
        onClick = {
            tabNavigator.current = tab


        },
        icon = {
            tab.options.icon?.apply {
                Icon(painter = this, contentDescription = tab.options.title)
            }
        },
        label = {
            Text(text = tab.options.title)
        }
    )
}

// TODO: onAppStateChanged property makes this class not serializable, causing crashes
private class TabsScreen(
    val onAppStateChanged: (AppState) -> Unit
) : Screen {

    @Composable
    override fun Content() {
        TabNavigator(HeadlineTab) { tabNavigator ->
            onAppStateChanged(
                AppState(
                    title = tabNavigator.current.options.title,
                    isNavigationIconEnabled = false,
                    isNavigationActionsEnabled = true
                )
            )

            Scaffold(
                content = {
                    CurrentTab()
                },
                bottomBar = {
                    NavigationBar {
                        TabNavigationItem(HeadlineTab)
                        TabNavigationItem(SourceTab)
                    }
                },
            )
        }
    }
}

data class AppState(
    val title: String = "",
    val isNavigationIconEnabled: Boolean = false,
    val isNavigationActionsEnabled: Boolean = false
)


