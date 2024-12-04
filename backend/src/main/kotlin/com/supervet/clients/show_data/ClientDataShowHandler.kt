package com.supervet.clients.show_data


import com.supervet.ktor.Handler
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class ClientDataShowHandler(private val clientDataShow: ClientDataShow) : Handler {

    override suspend fun invoke(ctx: RoutingContext) {
        val clientDataShowRequest = ctx.call.receive<ClientDataShowRequest>()

        try {
            val client = clientDataShow(clientDataShowRequest)
            ctx.call.respond(HttpStatusCode.OK, client)
        } catch (e: Exception) {
            ctx.application.log.error(e.stackTraceToString())
            when (e) {
                is ClientDoesNotExistException -> ctx.call.respond(HttpStatusCode.NotFound)
                else -> ctx.call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}


data class ClientDataShowRequest(
    val userId: String,
)