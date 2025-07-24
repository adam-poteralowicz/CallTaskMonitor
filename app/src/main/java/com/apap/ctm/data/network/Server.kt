package com.apap.ctm.data.network

import com.apap.ctm.domain.model.MonitorLogEntity
import com.apap.ctm.domain.model.MonitorLogEntryEntity
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

fun Application.setUp(controller: CallTaskController) {
    install(ContentNegotiation) { gson {} }
    routing {
        get("/") {
            controller.getServices()?.let {
                call.respond(HttpStatusCode.OK, it)
            } ?: call.respondText(
                contentType = ContentType.parse("text/html"),
                text = """
                    <h3>NO SERVICES AVAILABLE</h3>
                """.trimIndent()
            )
        }
        get("/status") {
            controller.getStatus()?.let {
                call.respond(HttpStatusCode.OK, it)
            } ?: call.respondText(
                contentType = ContentType.parse("text/html"),
                text = """
                    <h3>NO ONGOING CALL</h3>
                """.trimIndent()
            )
        }
        get("/log") {
            controller.getLog()?.let { result ->
                val updatedLog = updateTimesQueried(result)
                controller.insertLog(updatedLog)
                call.respond(HttpStatusCode.OK, updatedLog)
            } ?: call.respondText(
                contentType = ContentType.parse("text/html"),
                text = """
                    <h3>NO LOGS AVAILABLE</h3>
                """.trimIndent()
            )
        }
    }
}

private fun updateTimesQueried(result: MonitorLogEntity): MonitorLogEntity {
    val log = result.copy()
    val entries = mutableListOf<MonitorLogEntryEntity>()
    result.entries?.forEach { entry ->
        entry.timesQueried?.let {
            entries.add(entry.copy(timesQueried = it.plus(1)))
        } ?: entries.add(entry)
    }
    return log.copy(entries = entries)
}