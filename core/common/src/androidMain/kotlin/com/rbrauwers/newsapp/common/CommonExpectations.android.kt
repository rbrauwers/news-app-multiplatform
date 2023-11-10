import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

actual fun openUrl(url: String?) {
    val helper = object : KoinComponent { }
    val context: Context = helper.get()

    runCatching {
        Uri.parse(url)
    }.onSuccess {
        val intent = Intent(Intent.ACTION_VIEW, it).apply {
            flags = FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}