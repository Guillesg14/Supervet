package com.supervet.auth

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.supervet.acceptance.helpers.testApplicationWithDependencies
import com.supervet.auth.sign_in.ClinicSignInResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class SignInTest {
    @Test
    fun `should login a clinic`() = testApplicationWithDependencies { testRepository, jdbi, client, customConfig ->
        val clinicSignInPayload = mapOf(
            "email" to "${UUID.randomUUID()}@test.test",
            "password" to UUID.randomUUID().toString()
        )

        jdbi.useHandleUnchecked { handle ->
            handle.createUpdate(
                """
                    insert into users(id, email, password, type)
                    values(:id, :email, :password, :type)
                """.trimIndent()
            )
                .bind("id", UUID.randomUUID())
                .bind("email", clinicSignInPayload["email"])
                .bind(
                    "password",
                    BCrypt.withDefaults().hashToString(12, clinicSignInPayload["password"]?.toCharArray())
                )
                .bind("type", "CLINIC")
                .execute()
        }

        val response = client.post("auth/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(clinicSignInPayload)
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val jwt = JWT.require(Algorithm.HMAC512("supervet")).build().verify(response.body<ClinicSignInResponse>().token)

        assertEquals(jwt.claims["email"]?.asString(), clinicSignInPayload["email"])
        assertEquals(jwt.claims["type"]?.asString(), "CLINIC")
    }

    @Test
    fun `should login a client`() = testApplicationWithDependencies { testRepository, jdbi, client, customConfig ->
        val clientSignInPayload = mapOf(
            "email" to "${UUID.randomUUID()}@test.test",
            "password" to UUID.randomUUID().toString()
        )

        jdbi.useHandleUnchecked { handle ->
            handle.createUpdate(
                """
                    insert into users(id, email, password, type)
                    values(:id, :email, :password, :type)
                """.trimIndent()
            )
                .bind("id", UUID.randomUUID())
                .bind("email", clientSignInPayload["email"])
                .bind(
                    "password",
                    BCrypt.withDefaults().hashToString(12, clientSignInPayload["password"]?.toCharArray())
                )
                .bind("type", "CLIENT")
                .execute()
        }

        val response = client.post("auth/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(clientSignInPayload)
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val jwt = JWT.require(Algorithm.HMAC512("supervet")).build().verify(response.body<ClinicSignInResponse>().token)

        assertEquals(jwt.claims["email"]?.asString(), clientSignInPayload["email"])
        assertEquals(jwt.claims["type"]?.asString(), "CLIENT")
    }

    @Test
    fun `should return unauthorized if the password is not correct`() =
        testApplicationWithDependencies { testRepository, jdbi, client, customConfig ->
            val clinicSignInPayload = mapOf(
                "email" to "${UUID.randomUUID()}@test.test",
                "password" to UUID.randomUUID().toString()
            )

            jdbi.useHandleUnchecked { handle ->

                handle.createUpdate(
                    """
                        insert into users(id, email, password, type)
                        values(:id, :email, :password, :type)
                    """.trimIndent()
                )
                    .bind("id", UUID.randomUUID())
                    .bind("email", clinicSignInPayload["email"])
                    .bind(
                        "password",
                        BCrypt.withDefaults().hashToString(12, UUID.randomUUID().toString().toCharArray())
                    )
                    .bind("type", "CLINIC")
                    .execute()
            }

            val response = client.post("auth/sign-in") {
                contentType(ContentType.Application.Json)
                setBody(clinicSignInPayload)
            }

            assertEquals(HttpStatusCode.Unauthorized, response.status)
        }

    @Test
    fun `should return unauthorized if the user does not exist`() =
        testApplicationWithDependencies { testRepository, jdbi, client, customConfig ->
            val clinicSignInPayload = mapOf(
                "email" to "${UUID.randomUUID()}@test.test",
                "password" to UUID.randomUUID().toString()
            )

            val response = client.post("auth/sign-in") {
                contentType(ContentType.Application.Json)
                setBody(clinicSignInPayload)
            }

            assertEquals(HttpStatusCode.Unauthorized, response.status)
        }
}