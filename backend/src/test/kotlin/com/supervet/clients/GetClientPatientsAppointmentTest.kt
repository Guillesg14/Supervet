package com.supervet.clients

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


class GetClientPatientsAppointmentTest {

    @Test
    fun `should get clients patient appointments`() =
        testApplicationWithDependencies { testRepository, jdbi, httpClient, customConfig ->
            val clinic = testRepository.createClinic()
            val client = testRepository.createClient(clinic)
            val patient = testRepository.createPatient(client)
            val appointment = testRepository.createAppointment(patient)

            val token = JWT.create()
                .withAudience("supervet")
                .withIssuer("supervet")
                .withClaim("type", "CLIENT")
                .withClaim("user_id", client.userId.toString())
                .withClaim("email", client.email)
                .sign(Algorithm.HMAC512("supervet"))

            val response = httpClient.get("clients/appointments") {
                bearerAuth(token)
            }
            assertEquals(HttpStatusCode.OK, response.status)

            val appointmentsResponse = response.body<List<Map<String, String>>>()
            val appointmentResponse = appointmentsResponse.find { it["appointmentId"] == appointment.id.toString() }

            appointmentResponse.shouldNotBeNull()
            appointmentResponse["appointment"] shouldBe appointment.appointment
        }
}