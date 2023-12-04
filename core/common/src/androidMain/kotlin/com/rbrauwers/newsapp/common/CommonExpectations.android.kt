import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.text.SimpleDateFormat
import java.util.Date

actual fun openUrl(url: String?) {
    val helper = object : KoinComponent {}
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

actual fun Instant.format(dateFormat: DateTimeFormat, timeFormat: DateTimeFormat): String {
    val formatter = SimpleDateFormat.getDateTimeInstance(
        dateFormat.toSimpleDateFormat(),
        timeFormat.toSimpleDateFormat()
    )

    return formatter.format(Date(this.toEpochMilliseconds()))
}

private fun DateTimeFormat.toSimpleDateFormat(): Int {
    return when (this) {
        DateTimeFormat.Long -> {
            SimpleDateFormat.LONG
        }

        DateTimeFormat.Medium -> {
            SimpleDateFormat.MEDIUM
        }

        DateTimeFormat.Short -> {
            SimpleDateFormat.SHORT
        }
    }
}
