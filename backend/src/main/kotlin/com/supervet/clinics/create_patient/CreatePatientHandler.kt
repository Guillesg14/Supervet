package com.supervet.clinics.create_patient

import com.supervet.ktor.Handler
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

class CreatePatientHandler(private val createPatient: CreatePatient) : Handler{
    override suspend fun invoke(ctx: RoutingContext) {
        val clinicId = UUID.fromString(ctx.call.principal<JWTPrincipal>()!!.payload.getClaim("user_id").asString())
        val addPatientRequest = ctx.call.receive<CreatePatientRequest>()

        try {
            createPatient(addPatientRequest, clinicId)
            ctx.call.respond(HttpStatusCode.Created)
        } catch (e: Exception) {
            when (e) {
                is ClientDoesNotBelongToClinicException -> ctx.call.respond(HttpStatusCode.Unauthorized)
                else -> ctx.call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}
data class CreatePatientRequest(
    val clientId: String,
    val name: String,
    val breed: String,
    val age: String,
    val weight: Int,
)