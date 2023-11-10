package com.rbrauwers.newsapp.common.converters

import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant

/**
 * Converts a string to date, using the default format of News API.
 */
class ConvertStringToDate {

    operator fun invoke(string: String?): Instant? {
        string ?: return null
        return string.toInstant()
    }

}