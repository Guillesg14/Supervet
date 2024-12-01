package com.supervet.auth.data.show_clients


import com.supervet.ktor.Handler
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class ClientsShowHandler(private val clientsShow: ClientsShow) : Handler {

    override suspend fun invoke(ctx: RoutingContext) {
        val clientsShowRequest = ctx.call.receive<ClientsShowRequest>()

        try {
            val clients = clientsShow(clientsShowRequest)
            ctx.call.respond(HttpStatusCode.OK, clients) // Devuelve los clientes encontrados
        } catch (e: Exception) {
            ctx.application.log.error(e.stackTraceToString())
            when (e) {
                is ClientsDoesNotExistException -> ctx.call.respond(HttpStatusCode.NotFound)
                else -> ctx.call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}


data class ClientsShowRequest(
    val clinicId: String,
)