package com.supervet.clients.get_info

import com.supervet.ktor.Handler
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

class GetInfoHandler(private val getInfo: GetInfo) : Handler {

    override suspend fun invoke(ctx: RoutingContext) {
        val clientId = UUID.fromString(ctx.call.principal<JWTPrincipal>()!!.payload.getClaim("user_id").asString())

        try {
            val client = getInfo(clientId)
            ctx.call.respond(HttpStatusCode.OK, client)
        } catch (e: Exception) {
            when (e) {
                is ClientDoesNotExistException -> ctx.call.respond(HttpStatusCode.NotFound)
                else -> ctx.call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}
