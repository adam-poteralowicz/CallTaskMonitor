package com.apap.ctm.data.network

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.callTaskController() {
    routing {
        get("/") {
            call.respond(mapOf("message" to "CALL TASK MONITOR"))
        }
        get("/status") {
            call.respond(mapOf("status" to "ONLINE"))
        }
        get("/log") {
            call.respond(mapOf("log" to "EMPTY"))
        }
    }
}
