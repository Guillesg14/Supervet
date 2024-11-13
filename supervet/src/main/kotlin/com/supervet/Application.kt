package com.supervet

import com.supervet.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureMigrations()
    configureSecurity()
    configureRouting()
}
