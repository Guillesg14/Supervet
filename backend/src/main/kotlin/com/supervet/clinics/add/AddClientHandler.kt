package com.supervet.clinics.add

import com.supervet.ktor.Handler
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class AddClientHandler(private val addClient: AddClient) : Handler {

    override suspend fun invoke(ctx: RoutingContext) {
        val addClientRequest = ctx.call.receive<AddClientRequest>()
        try {
            addClient(addClientRequest)
            ctx.call.respond(HttpStatusCode.Created)
        } catch (e: Exception){
            when(e) {
                is ClientAlreadyExistsException -> ctx.call.respond(HttpStatusCode.Conflict)
                else -> ctx.call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}

data class AddClientRequest(val clinicId: String, val name: String, val surname: String, val phone: Number, val email: String )