package com.supervet.clinics.show_clients


import com.supervet.ktor.Handler
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class ClientsShowHandler(private val clientsShow: ClientsShow) : Handler {

    override suspend fun invoke(ctx: RoutingContext) {
        val clientsShowRequest = ctx.call.receive<ClientsShowRequest>()

        val clients = clientsShow(clientsShowRequest)
        ctx.call.respond(HttpStatusCode.OK, clients)
    }
}


data class ClientsShowRequest(
    val clinicId: String,
)