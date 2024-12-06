package com.supervet.clients.get_patients

import com.supervet.ktor.Handler
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

class GetClientPatientsHandler(private val getClientPatients: GetClientPatients): Handler {
    override suspend fun invoke(ctx: RoutingContext) {
        val clientUserId = UUID.fromString(ctx.call.principal<JWTPrincipal>()!!.payload.getClaim("user_id").asString())
        val patients = getClientPatients(clientUserId)
        ctx.call.respond(HttpStatusCode.OK, patients)
    }

}