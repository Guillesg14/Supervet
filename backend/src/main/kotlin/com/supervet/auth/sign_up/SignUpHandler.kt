package com.supervet.auth.sign_up

import at.favre.lib.crypto.bcrypt.BCrypt
import com.supervet.ktor.Handler
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import java.util.*

class SignUpHandler(private val jdbi: Jdbi) : Handler {
    override suspend fun invoke(ctx: RoutingContext) {
        val clinicSignUpRequest = ctx.call.receive<ClinicSignUpRequest>()

        jdbi.useHandleUnchecked { handle ->
            handle.createUpdate(
                """
                    insert into users(id, email, password, created_at)
                    values(:id, :email, :password, now())
                """.trimIndent()
            )
                .bind("id", UUID.randomUUID())
                .bind("email", clinicSignUpRequest.email)
                .bind("password", BCrypt.withDefaults().hashToString(12, clinicSignUpRequest.password.toCharArray()))
                .execute()
        }

        ctx.call.respond(HttpStatusCode.Created)
    }
}

data class ClinicSignUpRequest(val email: String, val password: String)