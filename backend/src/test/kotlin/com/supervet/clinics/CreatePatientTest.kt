package com.supervet.clinics

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.supervet.acceptance.helpers.testApplicationWithDependencies
import io.ktor.client.request.*
import io.ktor.http.*
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.jdbi.v3.core.mapper.MapMapper
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertNotNull

class CreatePatientTest {
    @Test
    fun `should add a new patient`() = testApplicationWithDependencies { testRepository, jdbi, httpClient, customConfig ->
        val clinic = testRepository.createClinic()
        val client = testRepository.createClient(clinic)

        val createPatientPayload = mapOf(
            "clientId" to client.id.toString(),
            "name" to "Buddy",
            "breed" to "Golden Retriever",
            "age" to "3",
            "weight" to 25,
        )

        val token = JWT.create()
            .withAudience("supervet")
            .withIssuer("supervet")
            .withClaim("type", "CLINIC")
            .withClaim("user_id", clinic.userId.toString())
            .withClaim("email", clinic.email)
            .sign(Algorithm.HMAC512("supervet"))

        val response = httpClient.post("clinics/create-patient") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(createPatientPayload)
        }

        assertEquals(HttpStatusCode.Created, response.status)

        val createdPatient = jdbi.withHandleUnchecked { handle ->
            handle.createQuery(
                """
                select name, breed, age, weight, client_id
                from patients
                where client_id = :clientId
                """.trimIndent()
            )
                .bind("clientId", client.id)
                .map(MapMapper())
                .one()
        }

        assertNotNull(createdPatient)
        assertEquals(createPatientPayload["clientId"], createdPatient["client_id"].toString())
        assertEquals(createPatientPayload["name"], createdPatient["name"])
        assertEquals(createPatientPayload["breed"], createdPatient["breed"])
        assertEquals(createPatientPayload["age"], createdPatient["age"])
        assertEquals(createPatientPayload["weight"], createdPatient["weight"])
    }

    @Test
    fun `should not add a new patient if the client does not belong to the clinic`() =
        testApplicationWithDependencies { testRepository, jdbi, httpClient, customConfig ->
            val clinic = testRepository.createClinic()
            val otherClinic = testRepository.createClinic()
            val client = testRepository.createClient(otherClinic)

            val createPatientPayload = mapOf(
                "clientId" to client.id.toString(),
                "name" to "Buddy",
                "breed" to "Golden Retriever",
                "age" to "3",
                "weight" to 25,
            )

            val token = JWT.create()
                .withAudience("supervet")
                .withIssuer("supervet")
                .withClaim("type", "CLINIC")
                .withClaim("user_id", clinic.userId.toString())
                .withClaim("email", clinic.email)
                .sign(Algorithm.HMAC512("supervet"))

            val response = httpClient.post("clinics/create-patient") {
                bearerAuth(token)
                contentType(ContentType.Application.Json)
                setBody(createPatientPayload)
            }

            assertEquals(HttpStatusCode.Unauthorized, response.status)

            assertThrows<IllegalStateException> {
                jdbi.withHandleUnchecked { handle ->
                    handle.createQuery(
                        """
                select name, breed, age, weight, client_id
                from patients
                where client_id = :clientId
                """.trimIndent()
                    )
                        .bind("clientId", client.id)
                        .map(MapMapper())
                        .one()
                }
            }
        }
}