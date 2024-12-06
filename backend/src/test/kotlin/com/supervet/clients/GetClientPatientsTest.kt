package com.supervet.clients

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.supervet.acceptance.helpers.testApplicationWithDependencies
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.junit.Test
import kotlin.test.assertEquals

class GetClientPatientsTest {
    @Test
    fun `should get clients patients`() =
        testApplicationWithDependencies { testRepository, jdbi, httpClient, customConfig ->
            val clinic = testRepository.createClinic()
            val client = testRepository.createClient(clinic)
            val patient = testRepository.createPatient(client)

            val token = JWT.create()
                .withAudience("supervet")
                .withIssuer("supervet")
                .withClaim("type", "CLIENT")
                .withClaim("user_id", client.userId.toString())
                .withClaim("email", client.email)
                .sign(Algorithm.HMAC512("supervet"))

            val response = httpClient.get("clients/patients") {
                bearerAuth(token)
            }

            assertEquals(HttpStatusCode.OK, response.status)

            val patientsResponse = response.body<List<Map<String, String>>>()

            assertEquals(1, patientsResponse.size)

            val patientResponse = patientsResponse.find { it["clientId"] == client.id.toString() }

            patientResponse.shouldNotBeNull()
            patientResponse["name"] shouldBe patient.name
            patientResponse["breed"] shouldBe patient.breed
            patientResponse["age"] shouldBe patient.age.toString()
            patientResponse["weight"] shouldBe patient.weight.toString()
        }
}