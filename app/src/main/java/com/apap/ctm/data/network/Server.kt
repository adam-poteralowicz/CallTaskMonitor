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
            call.respond(controller.getServices())
        }
        get("/status") {
            call.respond(controller.getStatus())
        }
        get("/log") {
            call.respond(controller.getLog())
        }
    }
}
