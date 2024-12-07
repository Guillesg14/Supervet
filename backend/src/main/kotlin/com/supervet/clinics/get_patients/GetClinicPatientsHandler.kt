package com.supervet.clinics.get_patients


import com.supervet.ktor.Handler
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

class GetClinicPatientsHandler (private val getClinicPatients: GetClinicPatients): Handler {
    override suspend fun invoke(ctx: RoutingContext){
        val clientId = UUID.fromString(ctx.call.parameters["client-id"]!!)
        val patients = getClinicPatients(clientId)
        ctx.call.respond(HttpStatusCode.OK, patients)
    }
}