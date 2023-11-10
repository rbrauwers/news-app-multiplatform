import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@Suppress("UNNECESSARY_SAFE_CALL")
actual fun openUrl(url: String?) {
    url ?: return
    NSURL(string = url)?.apply {
        UIApplication.sharedApplication.openURL(this)
    }
}