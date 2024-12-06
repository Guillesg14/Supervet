package com.supervet.clinics

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.supervet.acceptance.helpers.testApplicationWithDependencies
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals



class GetPatientTest {
    @Test
    fun `should get patient info`() =
        testApplicationWithDependencies { testRepository, jdbi, httpClient, customConfig ->
            val clinic = testRepository.createClinic()
            val client = testRepository.createClient(clinic)
            val createdPatient = testRepository.createPatient(client)

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

            val   patientsResponse =   response.body<List<Map<String, String>>>()

            assertEquals(1, patientsResponse.size)

            val patient = patientsResponse.find { it["id"] == createdPatient.clientId.toString()}
                patient shouldNotBe null
                patient!!["name"] shouldBe createdPatient.name
                patient["breed"] shouldBe createdPatient.breed
                patient["age"] shouldBe createdPatient.age
                patient["weight"] shouldBe createdPatient.weight
                patient["status"] shouldBe createdPatient.status
            }
}
