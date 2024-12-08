package com.supervet.clients.get_clinic_info

import com.supervet.ktor.Handler
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*
class GetClinicInfoHandler (private val getClinicInfo: GetClinicInfo) : Handler {
    override suspend fun invoke(ctx: RoutingContext){

        val userId = UUID.fromString(ctx.call.principal<JWTPrincipal>()!!.payload.getClaim("user_id").asString())
        ctx.call.application.log.info("Processing request for user ID: $userId")

        try {
            val clinicInfo = getClinicInfo(userId)
            ctx.call.respond(HttpStatusCode.OK, clinicInfo)
        } catch (e: Exception){
            when (e) {
                is ClientDoesNotExistException -> ctx.call.respond(HttpStatusCode.NotFound)
                else -> ctx.call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}