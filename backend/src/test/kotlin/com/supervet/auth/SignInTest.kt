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
        val clinic = testRepository.createClinic()

        val clinicSignInPayload = mapOf(
            "email" to clinic.email,
            "password" to clinic.password,
        )

        val response = client.post("auth/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(clinicSignInPayload)
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val jwt = JWT.require(Algorithm.HMAC512("supervet")).build().verify(response.body<ClinicSignInResponse>().token)

        assertEquals(jwt.claims["email"]?.asString(),clinic.email)
        assertEquals(jwt.claims["type"]?.asString(), "CLINIC")
    }

    @Test
    fun `should login a client`() = testApplicationWithDependencies { testRepository, jdbi, httpClient, customConfig ->
        val clinic = testRepository.createClinic()
        val client = testRepository.createClient(clinic)

        val clientSignInPayload = mapOf(
            "email" to client.email,
            "password" to client.password,
        )

        val response = httpClient.post("auth/sign-in") {
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
            val clinic = testRepository.createClinic()

            val clinicSignInPayload = mapOf(
                "email" to clinic.email,
                "password" to UUID.randomUUID().toString()
            )

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