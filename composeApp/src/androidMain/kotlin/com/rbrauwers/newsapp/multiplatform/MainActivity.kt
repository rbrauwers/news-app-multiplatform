package com.rbrauwers.newsapp.multiplatform

import App
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import com.arkivanov.decompose.defaultComponentContext
import components.AppRootComponent

/**
 * FragmentActivity is used over ComponentActivity because it is required by BiometricPrompt.
 * It is not an issue because FragmentActivity extends ComponentActivity.
 */
class MainActivity : FragmentActivity() {
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