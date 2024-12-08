package com.supervet.clinics.get_appointments

import com.supervet.ktor.Handler
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

class GetClinicPatientAppointmentsHandler (private val getClinicPatientAppointments: GetClinicPatientAppointments): Handler {
    override suspend fun invoke(ctx: RoutingContext) {
        val patientId = UUID.fromString(ctx.call.parameters["patient-id"]!!)
        val appointments = getClinicPatientAppointments(patientId)
        ctx.call.respond(HttpStatusCode.OK, appointments)
    }
}