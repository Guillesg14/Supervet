package com.example.auth.sign_up

import at.favre.lib.crypto.bcrypt.BCrypt
import com.supervet.ktor.Handler
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import org.jdbi.v3.postgres.PostgresPlugin
import java.util.*

class SignUpHandler : Handler {
    override suspend fun invoke(ctx: RoutingContext) {
        val clinicSignUpRequest = ctx.call.receive<ClinicSignUpRequest>()

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