package com.supervet.auth.clinics.sign_in

import com.supervet.ktor.Handler
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class SignInHandler(private val clinicSignIn: SignIn) : Handler {
    override suspend fun invoke(ctx: RoutingContext) {
        val clinicSignInRequest = ctx.call.receive<ClinicSignInRequest>()
        try {
            val token = clinicSignIn(clinicSignInRequest)
            ctx.call.respond(HttpStatusCode.OK, ClinicSignInResponse(token = token))
        } catch (e: Exception) {
            when (e) {
                is UserDoesNotExistException,
                is WrongPasswordException -> ctx.call.respond(HttpStatusCode.Unauthorized)

                else -> ctx.call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}

data class ClinicSignInRequest(val email: String, val password: String)
data class ClinicSignInResponse(val token: String)