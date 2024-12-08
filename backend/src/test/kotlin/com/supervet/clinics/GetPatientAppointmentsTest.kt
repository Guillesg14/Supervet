package com.supervet.clinics

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.supervet.acceptance.helpers.testApplicationWithDependencies
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals


class GetPatientAppointmentsTest {

    @Test
    fun `should get patient appointments`() =
        testApplicationWithDependencies { testRepository, jdbi, httpClient, customConfig ->
            val clinic = testRepository.createClinic()
            val client = testRepository.createClient(clinic)
            val patient = testRepository.createPatient(client)
            val appointment = testRepository.createAppointment(patient)

            val token = JWT.create()
                .withAudience("supervet")
                .withIssuer("supervet")
                .withClaim("type", "CLINIC")
                .withClaim("user_id", clinic.userId.toString())
                .withClaim("email", clinic.email)
                .sign(Algorithm.HMAC512("supervet"))

            val response = httpClient.get("clinics/clients/${client.id}/patients/${patient.id}/appointments") {
                bearerAuth(token)
            }
            assertEquals(HttpStatusCode.OK, response.status)

            val appointmentsResponse = response.body<List<Map<String, String>>>()
            val appointmentResponse = appointmentsResponse.find { it["id"] == appointment.id.toString() }

            appointmentResponse.shouldNotBeNull()
            appointmentResponse["appointment"] shouldBe appointment.appointment
            appointmentResponse["createdAt"] shouldNotBe null
        }
}