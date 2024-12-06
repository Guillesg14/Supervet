package com.supervet.clinics.get_patients


import com.supervet.ktor.Handler
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

class GetPatientsHandler (private val getPatients: GetPatients): Handler {
    override suspend fun invoke(ctx: RoutingContext){
        val clientId = UUID.fromString(ctx.call.parameters["client-id"]!!)
        val patients = getPatients(clientId)
        ctx.call.respond(OK, patients)
    }
}