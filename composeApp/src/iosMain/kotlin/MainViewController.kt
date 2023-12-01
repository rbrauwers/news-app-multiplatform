import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import components.AppRootComponent

fun MainViewController() = ComposeUIViewController {
    val rootComponent = remember {
        AppRootComponent(DefaultComponentContext(LifecycleRegistry()))
    }

    App(rootComponent)
}
