import com.rbrauwers.newsapp.network.di.networkModule
import org.koin.core.context.startKoin

@Suppress("unused")
fun initKoin() {
    startKoin {
        modules(networkModule)
    }
}