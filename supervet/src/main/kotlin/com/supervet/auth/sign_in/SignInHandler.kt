package com.example.auth.sign_in

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.supervet.ktor.Handler
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.jdbi.v3.postgres.PostgresPlugin
import java.util.*

class SignInHandler : Handler {
    override suspend fun invoke(ctx: RoutingContext) {
        val clinicSignInRequest = ctx.call.receive<ClinicSignInRequest>()
        val host = ctx.application.environment.config.property("database.host").getString()
        val port = ctx.application.environment.config.property("database.port").getString()
        val database = ctx.application.environment.config.property("database.database").getString()
        val user = ctx.application.environment.config.property("database.user").getString()
        val password = ctx.application.environment.config.property("database.password").getString()

        val hikariConfig = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://${host}:${port}/${database}?user=${user}&password=${password}"
            driverClassName = "org.postgresql.Driver"
        }

        val jdbi = Jdbi.create(HikariDataSource(hikariConfig))
            .installPlugin(PostgresPlugin())
            .installPlugin(KotlinPlugin(enableCoroutineSupport = true))

        val existingUser = jdbi.withHandleUnchecked { handle ->
            handle.createQuery(
                """
                    select id, email, password
                    from users
                    where email = :email
                """.trimIndent()
            )
                .bind("email", clinicSignInRequest.email)
                .map { rs, _ ->
                    User(
                        id = UUID.fromString(rs.getString("id")),
                        email = rs.getString("email"),
                        password = rs.getString("password")
                    )
                }
                .one()
        }

        if (!BCrypt.verifyer().verify(clinicSignInRequest.password.toCharArray(), existingUser.password).verified) {
            ctx.call.respond(HttpStatusCode.Unauthorized)
            return
        }

        val token = JWT.create()
            .withAudience("supervet")
            .withIssuer("supervet")
            .withClaim("type", "CLINIC")
            .withClaim("user_id", existingUser.id.toString())
            .withClaim("email", existingUser.email)
            .sign(Algorithm.HMAC512("supervet"))

        ctx.call.respond(HttpStatusCode.OK, ClinicSignInResponse(token = token))
    }
}

data class ClinicSignInRequest(val email: String, val password: String)
data class ClinicSignInResponse(val token: String)

data class User(val id: UUID, val email: String, val password: String)