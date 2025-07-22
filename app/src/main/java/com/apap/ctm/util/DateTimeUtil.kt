package com.apap.ctm.util

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

private const val TIMESTAMP_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ"
private val DATETIME_FORMATTER = DateTimeFormat.forPattern(TIMESTAMP_PATTERN)

fun String.toDateTime(): DateTime {
    return DateTime.parse(this, DATETIME_FORMATTER)
}

fun DateTime.toDateTimeString() : String = toString(TIMESTAMP_PATTERN)