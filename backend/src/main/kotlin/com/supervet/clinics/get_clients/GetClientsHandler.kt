package com.supervet.clinics.get_clients


import com.supervet.ktor.Handler
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

class GetClientsHandler(private val getClients: GetClients) : Handler {
    override suspend fun invoke(ctx: RoutingContext) {
        val clinicId = UUID.fromString(ctx.call.principal<JWTPrincipal>()!!.payload.getClaim("user_id").asString())

        val clients = getClients(clinicId)
        ctx.call.respond(HttpStatusCode.OK, clients)
    }
}