package com.supervet.ktor

import com.supervet.auth.sign_in.SignInHandler
import com.supervet.auth.clinics.sign_up.SignUpHandler
import com.supervet.auth.clients.sign_up.AddClientHandler
import com.supervet.auth.data.show_clients.ClientsShowHandler
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

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
            post("sign-in", executeInvoke<SignInHandler>())

            route("clients") {
                post("sign-up", executeInvoke<AddClientHandler>())
            }

            route("clinics") {
                post("sign-up", executeInvoke<SignUpHandler>())
            }
            route("data"){
                post("show_clients", executeInvoke<ClientsShowHandler>())
            }
        }
    }
}