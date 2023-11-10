import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.rbrauwers.newsapp.designsystem.theme.NewsAppTheme
import com.rbrauwers.newsapp.headlines.HeadlineTab
import com.rbrauwers.newsapp.sources.SourceTab

@Composable
fun App() {
    NewsAppTheme {
        TabNavigator(HeadlineTab) {
            Scaffold(
                content = {
                    CurrentTab()
                },
                bottomBar = {
                    NavigationBar {
                        TabNavigationItem(HeadlineTab)
                        TabNavigationItem(SourceTab)
                    }
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