package com.supervet.auth.patients.add

import com.supervet.ktor.Handler
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class AddPatientHandler(private val patientAdd: PatientAdd) : Handler{
    override suspend fun invoke(ctx: RoutingContext) {
        val addPatientRequest = ctx.call.receive<PatientAddRequest>()
        try {
            patientAdd(addPatientRequest)
            ctx.call.respond(HttpStatusCode.Created)
        } catch (e: Exception) {
            when (e) {
                is UserAlreadyExistsException -> ctx.call.respond(HttpStatusCode.Conflict)
                else -> ctx.call.respond(HttpStatusCode.InternalServerError)
            }
        }

    }
}
data class PatientAddRequest(
    val owner_id: String,
    val owner_name: String,
    val patient_id: String,
    val name: String,
    val breed: String,
    val age: String,
    val weight: Int,
    val status: String,
    val appointment: String
)