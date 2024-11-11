package com.supervet.plugins


import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.jdbi.v3.postgres.PostgresPlugin
import java.util.UUID

fun Application.configureRouting() {
    install(ContentNegotiation) {
        jackson()
    }

    routing {
        post("/clinics/sign-up") {
            val clinicSignUpRequest = call.receive<ClinicSignUpRequest>()

            val hikariConfig = HikariConfig().apply {
                jdbcUrl = environment.config.property("database.connectionString").getString()
                driverClassName = "org.postgresql.Driver"
            }

            val jdbi = Jdbi.create(HikariDataSource(hikariConfig))
                .installPlugin(PostgresPlugin())
                .installPlugin(KotlinPlugin(enableCoroutineSupport = true))

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

            call.respond(HttpStatusCode.Created)
        }

        post("/clinics/sign-in") {
            val clinicSignInRequest = call.receive<ClinicSignInRequest>()

            val hikariConfig = HikariConfig().apply {
                jdbcUrl = environment.config.property("database.connectionString").getString()
                driverClassName = "org.postgresql.Driver"
            }

            val jdbi = Jdbi.create(HikariDataSource(hikariConfig))
                .installPlugin(PostgresPlugin())
                .installPlugin(KotlinPlugin(enableCoroutineSupport = true))

            val user = jdbi.withHandleUnchecked { handle ->
                handle.createQuery(
                    """
                        select id, email, password
                        from users
                        where email = :email
                    """.trimIndent()
                )
                    .bind("email", clinicSignInRequest.email)
                    .map { rs, _ -> User(
                        id = UUID.fromString(rs.getString("id")),
                        email = rs.getString("email"),
                        password = rs.getString("password")
                    ) }
                    .one()
            }

            if (!BCrypt.verifyer().verify(clinicSignInRequest.password.toCharArray(), user.password).verified) {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }

            val token = JWT.create()
                .withAudience("supervet")
                .withIssuer("supervet")
                .withClaim("type", "clinic")
                .withClaim("user_id", user.id.toString())
                .withClaim("email", user.email)
                .sign(Algorithm.HMAC512("supervet"))

            call.respond(HttpStatusCode.OK, ClinicSignInResponse(token = token))
        }
    }
}

data class ClinicSignUpRequest(val email: String, val password: String)
data class ClinicSignInRequest(val email: String, val password: String)
data class ClinicSignInResponse(val token: String)

data class User(val id: UUID, val email: String, val password: String)