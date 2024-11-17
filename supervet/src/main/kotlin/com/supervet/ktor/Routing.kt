package com.supervet.ktor

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.auth.sign_in.SignInHandler
import com.example.auth.sign_up.SignUpHandler
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.jdbi.v3.postgres.PostgresPlugin
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import java.util.UUID

inline fun <reified T : Handler> executeInvoke(): suspend RoutingContext.() -> Unit = {
    val handler by this.call.application.closestDI().instance<T>()
    handler(this)
}

interface Handler {
    suspend operator fun invoke(ctx: RoutingContext)
}

fun Application.configureRouting() {
    install(ContentNegotiation) {
        jackson()
    }

    routing {
        route("auth") {
            route("clinics") {
                post("sign-up", executeInvoke<SignUpHandler>())
                post("sign-in", executeInvoke<SignInHandler>())
            }
        }
    }
}