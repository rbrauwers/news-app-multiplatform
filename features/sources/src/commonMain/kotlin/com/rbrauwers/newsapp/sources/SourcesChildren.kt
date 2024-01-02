package com.rbrauwers.newsapp.sources

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation

@Composable
fun SourcesChildren(component: SourcesComponent, modifier: Modifier = Modifier) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation(fade())
    ) {
        when (val child = it.instance) {
            is SourcesComponent.SourcesChild.SourcesList -> {
                SourcesListScreen(
                    component = child.component,
                    onNavigateToInfo = component.onNavigateToInfo,
                    onNavigateToSource = component::onNavigateToSource,
                    modifier = modifier
                )
            }

            is SourcesComponent.SourcesChild.SourceDetail -> {
                SourceDetailScreen(
                    component = child.component,
                    onNavigateBack = component::onNavigateBack,
                    modifier = modifier
                )
            }
        }
    }
}