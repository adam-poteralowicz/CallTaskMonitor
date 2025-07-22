package com.apap.ctm.data.network

import io.ktor.serialization.gson.gson
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.setUp(controller: CallTaskController) {
    install(ContentNegotiation) { gson {} }
    routing {
        get("/") {
            controller.getServices()?.let {
                call.respond(it)
            } ?: call.respond("NO SERVICES ONLINE")
        }
        get("/status") {
            controller.getStatus()?.let {
                call.respond(it)
            } ?: call.respond("NO CALL IN PROGRESS")
        }
        get("/log") {
            controller.getLog()?.let {
                call.respond(it)
            } ?: call.respond("NO LOGS AVAILABLE")
        }
    }
}