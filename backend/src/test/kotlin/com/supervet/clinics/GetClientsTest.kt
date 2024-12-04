package com.supervet.clinics

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.supervet.acceptance.helpers.testApplicationWithDependencies
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals


class GetClientsTest {
    @Test
    fun `should get clients list`() = testApplicationWithDependencies { testRepository, jdbi, httpClient, customConfig ->
        val clinic = testRepository.createClinic()
        val client = testRepository.createClient(clinic)

        val token = JWT.create()
            .withAudience("supervet")
            .withIssuer("supervet")
            .withClaim("type", "CLINIC")
            .withClaim("user_id", clinic.userId.toString())
            .withClaim("email", clinic.email)
            .sign(Algorithm.HMAC512("supervet"))

        val response = httpClient.get("/clinics/clients") {
            bearerAuth(token)
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val clientsResponse = response.body<List<Map<String, String>>>()

        assertEquals(1, clientsResponse.size)

        clientsResponse.find { it["id"] == client.id.toString() }.let {
            it shouldNotBe null
            it?.get("name") shouldBe client.name
            it?.get("surname") shouldBe client.surname
            it?.get("phone") shouldBe client.phone
        }
    }
}
