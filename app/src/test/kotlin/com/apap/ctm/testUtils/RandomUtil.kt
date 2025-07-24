package com.apap.ctm.testUtils

import com.apap.ctm.domain.model.MonitorLog
import com.apap.ctm.domain.model.MonitorLogEntry
import com.apap.ctm.domain.model.MonitorRoot
import com.apap.ctm.domain.model.MonitorService
import com.apap.ctm.domain.model.MonitorStatus
import com.apap.ctm.util.toDateTimeString
import org.joda.time.DateTime
import org.joda.time.Instant
import java.security.SecureRandom
import java.util.UUID

private fun randomBoolean() = SecureRandom().nextBoolean()
private fun randomInt() = SecureRandom().nextInt()
private fun randomLong() = SecureRandom().nextLong()
private fun randomNumber() = (1..9).map { randomInt() }
private fun randomString() = UUID.randomUUID().toString()
private fun randomDateTime() = DateTime(Instant.ofEpochMilli(randomLong()))

fun randomLog() = MonitorLog(
    entries = (1..10).map { randomLogEntry() }
)

fun randomLogEntry(timesQueried: Int = 0) = MonitorLogEntry(
    name = randomString(),
    number = randomNumber().joinToString(""),
    duration = randomInt().mod(3600),
    beginning = randomDateTime().toDateTimeString(),
    timesQueried = timesQueried
)

fun randomStatus(
    number: String? = null,
    ongoing: Boolean? = null,
    duration: Int? = null,
    name: String? = null,
    start: DateTime? = null,
    stop: DateTime? = null
) = MonitorStatus(
    number = number ?: randomNumber().joinToString(""),
    ongoing = ongoing ?: randomBoolean(),
    duration = duration ?: randomInt().mod(3600),
    name = name ?: randomString(),
    start = start?.toDateTimeString() ?: randomDateTime().toDateTimeString(),
    stop = stop?.toDateTimeString() ?: randomDateTime().toDateTimeString()
)

fun randomRoot() = MonitorRoot(
    start = randomDateTime().toDateTimeString(),
    services = listOf(randomService(), randomService())
)

fun randomService() = MonitorService(
    name = randomString(),
    uri = randomString()
)