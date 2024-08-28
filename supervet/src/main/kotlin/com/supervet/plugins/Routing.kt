package com.supervet.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/test") {
            call.respondText("texto de prueba")
        }
        get("/test/{id}") {
            val id = call.parameters["id"]
            call.respondText("el id es $id")
        }
    }
}
