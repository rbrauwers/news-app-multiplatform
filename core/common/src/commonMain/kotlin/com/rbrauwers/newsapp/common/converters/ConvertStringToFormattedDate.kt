package com.rbrauwers.newsapp.common.converters

import format

/**
 * Converts a string to formatted date, using default date time format.
 */
class ConvertStringToFormattedDate(
    private val convertStringToDate: ConvertStringToDate = ConvertStringToDate()
) {

    operator fun invoke(string: String?): String? {
        val instant = convertStringToDate(string) ?: return null
        return instant.format(dateFormat = DateTimeFormat.Medium, timeFormat = DateTimeFormat.Short)
    }

}
