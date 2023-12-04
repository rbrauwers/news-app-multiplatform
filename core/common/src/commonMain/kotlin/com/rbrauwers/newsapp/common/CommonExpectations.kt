import kotlinx.datetime.Instant

expect fun openUrl(url: String?)

expect fun Instant.format(dateFormat: DateTimeFormat, timeFormat: DateTimeFormat): String

enum class DateTimeFormat {
    Short, Medium, Long
}
