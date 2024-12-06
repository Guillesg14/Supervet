package com.supervet.clinics.delete_client

import com.supervet.ktor.Handler
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

class DeleteClientHandler(
    private val deleteClient: DeleteClient,
) : Handler {
    override suspend fun invoke(ctx: RoutingContext) {
        val clientId = UUID.fromString(ctx.call.parameters["client-id"]!!)
        val clinicUserId = UUID.fromString(ctx.call.principal<JWTPrincipal>()!!.payload.getClaim("user_id").asString())

        try {
            deleteClient(clientId, clinicUserId)
            ctx.call.respond(HttpStatusCode.NoContent)
        } catch (e: Exception) {
            when (e) {
                is ClientDoesNotBelongToClinicException -> ctx.call.respond(HttpStatusCode.Unauthorized)
            }
        }

    }
}