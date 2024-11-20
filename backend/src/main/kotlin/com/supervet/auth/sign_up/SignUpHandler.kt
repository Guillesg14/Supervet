package com.supervet.auth.sign_up

import com.supervet.ktor.Handler
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class SignUpHandler(private val signUp: SignUp) : Handler {
    override suspend fun invoke(ctx: RoutingContext) {
        val clinicSignUpRequest = ctx.call.receive<ClinicSignUpRequest>()
        signUp(clinicSignUpRequest)
        ctx.call.respond(HttpStatusCode.Created)
    }
}

data class ClinicSignUpRequest(val email: String, val password: String)