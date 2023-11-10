import di.appModule
import org.koin.core.context.startKoin

@Suppress("unused")
fun initKoin() {
    startKoin {
        modules(appModule)
    }
}