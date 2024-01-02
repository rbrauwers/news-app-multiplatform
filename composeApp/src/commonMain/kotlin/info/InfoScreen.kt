package info

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rbrauwers.newsapp.designsystem.BackNavigationIcon
import com.rbrauwers.newsapp.designsystem.BottomBarState
import com.rbrauwers.newsapp.designsystem.LocalAppState
import com.rbrauwers.newsapp.designsystem.NewsDefaultTopBar
import com.rbrauwers.newsapp.designsystem.TopBarState
import com.rbrauwers.newsapp.resources.MultiplatformResources
import dev.icerock.moko.resources.compose.stringResource
import openUrl

private data class Lib(
    val name: String,
    val url: String
)

private val libs = listOf(
    Lib(name = "Decompose", url = "https://arkivanov.github.io/Decompose/"),
    Lib(name = "Jetpack Compose", url = "https://www.jetbrains.com/lp/compose-multiplatform/"),
    Lib(name = "Kamel", url = "https://github.com/Kamel-Media/Kamel"),
    Lib(name = "Koin", url = "https://insert-koin.io/"),
    Lib(name = "Ktor", url = "https://ktor.io/"),
    Lib(name = "Material", url = "https://m3.material.io/develop/android/jetpack-compose"),
    Lib(name = "Moko Resources", url = "https://github.com/icerockdev/moko-resources"),
    Lib(name = "SQLDelight", url = "https://github.com/cashapp/sqldelight"),
)

@Composable
internal fun InfoScreen(
    onNavigateBack: () -> Unit
) {
    LocalAppState.current.apply {
        LaunchedEffect(Unit) {
            setTopBarState(
                topBarState = TopBarState(
                    title = { NewsDefaultTopBar(title = stringResource(MultiplatformResources.strings.app_info)) },
                    navigationIcon = {
                        BackNavigationIcon(onBackClick = onNavigateBack)
                    }
                )
            )
            setBottomBarState(bottomBarState = BottomBarState(isVisible = false))
        }
    }

    InfoScreenContent(modifier = Modifier.fillMaxSize())
}

@Composable
private fun InfoScreenContent(modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            item {
                Text(
                    text = "Libraries and frameworks",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Divider(modifier = Modifier.height(2.dp))
                Spacer(modifier = Modifier.height(4.dp))
            }

            items(
                items = libs
            ) { lib ->
                val isLast = lib == libs.last()

                Row(modifier = Modifier
                    .clickable {
                        openUrl(lib.url)
                    }
                    .padding(16.dp)
                ) {
                    Text(text = lib.name)

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (!isLast) {
                    Divider(modifier = Modifier.height(1.dp))
                }
            }
        }
    }
}