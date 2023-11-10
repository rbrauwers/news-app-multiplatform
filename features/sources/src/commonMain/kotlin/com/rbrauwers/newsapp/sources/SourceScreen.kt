package com.rbrauwers.newsapp.sources

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object SourceTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            //val title = stringResource(R.string.home_tab)
            //val icon = rememberVectorPainter(Icons.Default.Home)
            val title = "source hardcoded"
            val icon: Painter? = null

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Text("SourceTab::Content")
    }
}