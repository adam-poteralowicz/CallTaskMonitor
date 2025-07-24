package com.apap.ctm.testUtils

import com.apap.ctm.domain.model.MonitorLogEntry
import com.apap.ctm.util.toDateTimeString
import org.joda.time.DateTime
import org.joda.time.Instant
import java.security.SecureRandom
import java.util.UUID

fun randomLogEntry(timesQueried: Int = 0) = MonitorLogEntry(
    name = randomString(),
    number = randomNumber().joinToString(""),
    duration = randomInt().mod(3600),
    beginning = randomDateTime().toDateTimeString(),
    timesQueried = timesQueried
)

fun randomInt() = SecureRandom().nextInt()
fun randomNumber() = (1..9).map { randomInt() }
fun randomString() = UUID.randomUUID().toString()
fun randomDateTime() = DateTime(Instant.ofEpochMilli(randomLong()))
fun randomLong() = SecureRandom().nextLong()