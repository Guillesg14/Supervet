package com.supervet.auth.clinics.sign_up

import com.supervet.ktor.Handler
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class SignUpHandler(private val clinicSignUp: ClinicSignUp) : Handler {
    override suspend fun invoke(ctx: RoutingContext) {
        val clinicSignUpRequest = ctx.call.receive<ClinicSignUpRequest>()
        try {
            clinicSignUp(clinicSignUpRequest)
            ctx.call.respond(HttpStatusCode.Created)
        } catch (e: Exception) {
            when (e) {
                is UserAlreadyExistsException -> ctx.call.respond(HttpStatusCode.Conflict)
                else -> ctx.call.respond(HttpStatusCode.InternalServerError)
            }
        }

    }
}

data class ClinicSignUpRequest(
    val email: String,
    val password: String,
    val phone: Int,
    val address: String
)