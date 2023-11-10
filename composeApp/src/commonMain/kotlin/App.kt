import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.rbrauwers.newsapp.designsystem.theme.NewsAppTheme
import com.rbrauwers.newsapp.headlines.HeadlineTab
import com.rbrauwers.newsapp.sources.SourceTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    NewsAppTheme {
        TabNavigator(HeadlineTab) {
            Scaffold(
                content = { padding ->
                    Surface(modifier = Modifier.padding(padding)) {
                        CurrentTab()
                    }
                },
                bottomBar = {
                    NavigationBar {
                        TabNavigationItem(HeadlineTab)
                        TabNavigationItem(SourceTab)
                    }
                },
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = LocalTabNavigator.current.current.options.title,
                                style = MaterialTheme.typography.titleLarge
                            )
                        },
                        scrollBehavior = scrollBehavior,
                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                    )
                }
            )
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    NavigationBarItem(
        selected = tabNavigator.current.key == tab.key,
        onClick = { tabNavigator.current = tab },
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