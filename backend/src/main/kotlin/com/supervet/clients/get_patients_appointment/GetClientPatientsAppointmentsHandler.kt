package com.supervet.clients.get_patients_appointment

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*
import com.supervet.ktor.Handler

class GetClientPatientsAppointmentsHandler (private val getClientPatientsAppointments: GetClientPatientsAppointments): Handler {
    override suspend fun invoke(ctx: RoutingContext) {
        val userId = UUID.fromString(ctx.call.principal<JWTPrincipal>()!!.payload.getClaim("user_id").asString())
        val appointments= getClientPatientsAppointments(userId)
        ctx.call.respond(HttpStatusCode.OK, appointments)
}
    }