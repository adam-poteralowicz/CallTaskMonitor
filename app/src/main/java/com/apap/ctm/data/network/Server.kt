package com.apap.ctm.data.network

import com.apap.ctm.domain.model.MonitorLog
import com.apap.ctm.domain.model.MonitorLogEntry
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.gson.gson
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

private const val ROOT = "/"
private const val LOG = "/log"
private const val STATUS = "/status"

fun Application.setUp(controller: CallTaskController) = with(controller) {
    install(ContentNegotiation) { gson {} }
    routing {
        get(ROOT) {
            getServices()?.let {
                call.respond(HttpStatusCode.OK, it)
            } ?: call.respondText(
                contentType = ContentType.parse("text/html"),
                text = """
                    <h3>NO SERVICES AVAILABLE</h3>
                """.trimIndent()
            )
        }
        get(STATUS) {
            getStatus()?.let {
                call.respond(HttpStatusCode.OK, it)
            } ?: call.respondText(
                contentType = ContentType.parse("text/html"),
                text = """
                    <h3>NO ONGOING CALL</h3>
                """.trimIndent()
            )
        }
        get(LOG) {
            getLog().let { result ->
                clearEntries()
                val updatedLog = updateTimesQueried(
                    result,
                    onEntryUpdated = { entry ->
                        updateLogEntry(entry)
                    }
                )
                insertLog(updatedLog)
                call.respond(HttpStatusCode.OK, updatedLog)
            }
        }
    }
}

private suspend fun updateTimesQueried(result: MonitorLog, onEntryUpdated: suspend(MonitorLogEntry) -> Unit): MonitorLog {
    val log = result.copy()
    val entries = mutableListOf<MonitorLogEntry>()
    result.entries.forEach { entry ->
        val updatedEntry = entry.copy(timesQueried = entry.timesQueried.plus(1))
        entries.add(updatedEntry)
        onEntryUpdated(updatedEntry)
    }
    return log.copy(entries = entries)
}