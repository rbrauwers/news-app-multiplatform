package com.rbrauwers.newsapp.sources

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.rbrauwers.newsapp.designsystem.BackNavigationIcon
import com.rbrauwers.newsapp.designsystem.BadgedTopBar
import com.rbrauwers.newsapp.designsystem.BottomBarState
import com.rbrauwers.newsapp.designsystem.InfoActionButton
import com.rbrauwers.newsapp.designsystem.LocalAppState
import com.rbrauwers.newsapp.designsystem.NewsDefaultTopBar
import com.rbrauwers.newsapp.designsystem.TopBarState
import com.rbrauwers.newsapp.resources.MultiplatformResources
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun SourceDetailScreen(
    component: SourceDetailComponent,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LocalAppState.current.apply {
        LaunchedEffect(Unit) {
            setTopBarState(
                topBarState = TopBarState(
                    title = {
                        NewsDefaultTopBar(title = stringResource(MultiplatformResources.strings.source_details))
                    },
                    navigationIcon = {
                        BackNavigationIcon(onBackClick = onNavigateBack)
                    }
                )
            )

            setBottomBarState(bottomBarState = BottomBarState(isVisible = true))
        }
    }

    Text(text = component.source.name ?: "N/A", modifier = modifier)
}