package com.rbrauwers.newsapp.common.converters

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Converts a string to formatted date, using default date time format.
 */
class ConvertStringToDateTimeInstance(
    private val convertStringToDate: ConvertStringToDate = ConvertStringToDate()
) {

    operator fun invoke(string: String?): String? {
        val instant = convertStringToDate(string) ?: return null
        val date = instant.toLocalDateTime(TimeZone.UTC)
        return "${date.monthNumber}/${date.dayOfMonth}/${date.year} ${date.hour}:${date.minute}"
    }

}
