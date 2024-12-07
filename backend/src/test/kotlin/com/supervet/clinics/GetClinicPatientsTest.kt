package com.supervet.clinics

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.supervet.acceptance.helpers.testApplicationWithDependencies
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals


class GetClinicPatientsTest {
    @Test
    fun `should get clinics patients`() =
        testApplicationWithDependencies { testRepository, jdbi, httpClient, customConfig ->
            val clinic = testRepository.createClinic()
            val client = testRepository.createClient(clinic)
            val patient = testRepository.createPatient(client)

            val token = JWT.create()
                .withAudience("supervet")
                .withIssuer("supervet")
                .withClaim("type", "CLINIC")
                .withClaim("user_id", clinic.userId.toString())
                .withClaim("email", clinic.email)
                .sign(Algorithm.HMAC512("supervet"))

            val response = httpClient.get("clinics/clients/${client.id}/patients") {
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
