package com.rbrauwers.newsapp.multiplatform

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.defaultComponentContext
import components.AppRootComponent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootComponent = AppRootComponent(componentContext = defaultComponentContext())

        setContent {
            App(component = rootComponent)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {

}