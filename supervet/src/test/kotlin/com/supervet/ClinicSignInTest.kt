package com.supervet

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.supervet.acceptance.helpers.testApplicationWithDependencies
import com.supervet.plugins.ClinicSignInResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ClinicSignInTest {
    @Test
    fun `should login a clinic`() = testApplicationWithDependencies { jdbi, client, customConfig ->
        val signInPayload = object {
            val email = "${UUID.randomUUID()}@test.test"
            val password = UUID.randomUUID().toString()
        }

        jdbi.useHandleUnchecked { handle ->
            handle.createUpdate(
                """
                    insert into users(id, email, password, created_at)
                    values(:id, :email, :password, now())
                """.trimIndent()
            )
                .bind("id", UUID.randomUUID())
                .bind("email", signInPayload.email)
                .bind("password", BCrypt.withDefaults().hashToString(12, signInPayload.password.toCharArray()))
                .execute()
        }

        val response = client.post("/clinics/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(signInPayload)
        }

        assertEquals(OK, response.status)

        val jwt = JWT.require(Algorithm.HMAC512("supervet")).build().verify(response.body<ClinicSignInResponse>().token)

        assertEquals(jwt.claims["email"]?.asString(), signInPayload.email)
        assertEquals(jwt.claims["type"]?.asString(), "CLINIC")
    }

    @Test
    fun `should return unauthorized if the password is not correct`() = testApplicationWithDependencies { jdbi, client, customConfig ->
        val signInPayload = object {
            val email = "${UUID.randomUUID()}@test.test"
            val password = UUID.randomUUID().toString()
        }

        jdbi.useHandleUnchecked { handle ->
            handle.createUpdate(
                """
                    insert into users(id, email, password, created_at)
                    values(:id, :email, :password, now())
                """.trimIndent()
            )
                .bind("id", UUID.randomUUID())
                .bind("email", signInPayload.email)
                .bind("password", BCrypt.withDefaults().hashToString(12, UUID.randomUUID().toString().toCharArray()))
                .execute()
        }

        val response = client.post("/clinics/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(signInPayload)
        }

        assertEquals(Unauthorized, response.status)
    }
}
