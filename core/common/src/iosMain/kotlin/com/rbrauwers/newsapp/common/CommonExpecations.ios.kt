import kotlinx.datetime.Instant
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterLongStyle
import platform.Foundation.NSDateFormatterMediumStyle
import platform.Foundation.NSDateFormatterShortStyle
import platform.Foundation.NSURL
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.UIKit.UIApplication

@Suppress("UNNECESSARY_SAFE_CALL")
actual fun openUrl(url: String?) {
    url ?: return
    NSURL(string = url)?.apply {
        UIApplication.sharedApplication.openURL(this)
    }
}

actual fun Instant.format(dateFormat: DateTimeFormat, timeFormat: DateTimeFormat): String {
    val formatter = NSDateFormatter()
    formatter.dateStyle = dateFormat.toNSDateFormatterStyle()
    formatter.timeStyle = dateFormat.toNSDateFormatterStyle()

    val date = NSDate.dateWithTimeIntervalSince1970(this.epochSeconds.toDouble())
    return formatter.stringFromDate(date)
}

private fun DateTimeFormat.toNSDateFormatterStyle(): ULong {
    return when(this) {
        DateTimeFormat.Long -> {
            NSDateFormatterLongStyle
        }

        DateTimeFormat.Medium -> {
            NSDateFormatterMediumStyle
        }

        DateTimeFormat.Short -> {
            NSDateFormatterShortStyle
        }
    }
}
