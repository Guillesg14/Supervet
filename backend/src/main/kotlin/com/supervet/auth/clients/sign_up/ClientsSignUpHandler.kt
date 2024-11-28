
package com.supervet.auth.clients.sign_up

import com.supervet.ktor.Handler
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class AddClientHandler(private val clientSignUp: ClientSignUp) : Handler {

    override suspend fun invoke(ctx: RoutingContext) {
        val clientSignUpRequest = ctx.call.receive<ClientSignUpRequest>()

        try {
            clientSignUp(clientSignUpRequest)
            ctx.call.respond(HttpStatusCode.Created)
        } catch (e: Exception) {
            ctx.application.log.error(e.stackTraceToString())
            when (e) {
                is ClientAlreadyExistsException -> ctx.call.respond(HttpStatusCode.Conflict)
                is ClinicDoesNotExistException -> ctx.call.respond(HttpStatusCode.NotFound)
                else -> ctx.call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}

data class ClientSignUpRequest(
    val clinicId: String,
    val name: String,
    val surname: String,
    val phone: Number,
    val email: String,
    val password: String
)
