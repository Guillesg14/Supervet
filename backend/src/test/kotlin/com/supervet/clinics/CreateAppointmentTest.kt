package com.supervet.clinics

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.supervet.acceptance.helpers.testApplicationWithDependencies
import io.ktor.client.request.*
import io.ktor.http.*
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.jdbi.v3.core.mapper.MapMapper
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CreateAppointmentTest {

    @Test
    fun `should add a new appointment`() =
        testApplicationWithDependencies { testRepository, jdbi, httpClient, customConfig ->
            val clinic = testRepository.createClinic()
            val client = testRepository.createClient(clinic)
            val patient = testRepository.createPatient(client)

            val createAppointmentPayload = mapOf(
                "appointment" to "Test appointment text"
            )

            val token = JWT.create()
                .withAudience("supervet")
                .withIssuer("supervet")
                .withClaim("type", "CLINIC")
                .withClaim("user_id", clinic.userId.toString())
                .withClaim("email", clinic.email)
                .sign(Algorithm.HMAC512("supervet"))

            val response = httpClient.post("clinics/clients/${client.id}/patients/${patient.id}/appointments") {
                bearerAuth(token)
                contentType(ContentType.Application.Json)
                setBody(createAppointmentPayload)
            }

            assertEquals(HttpStatusCode.Created, response.status)

            val createdAppointment = jdbi.withHandleUnchecked { handle ->
                handle.createQuery(
                    """
                select appointment
                from appointments
                where patient_id = :patientId
                """.trimIndent()
                )
                    .bind("patientId", patient.id)
                    .map(MapMapper())
                    .one()
            }

            assertEquals(createAppointmentPayload["appointment"], createdAppointment["appointment"])
        }
}