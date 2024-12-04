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
    fun `should add a new patient`() = testApplicationWithDependencies { testRepository, jdbi, client, customConfig ->
        val clinicUserId = UUID.randomUUID()
        val clinicId = UUID.randomUUID()
        val clinicEmail = "${UUID.randomUUID()}@test.test"
        val clientUserId = UUID.randomUUID()
        val clientId = UUID.randomUUID()

        val createPatientPayload = mapOf(
            "clientId" to clientId.toString(),
            "name" to "Buddy",
            "breed" to "Golden Retriever",
            "age" to "3",
            "weight" to 25,
            "status" to "Healthy",
        )

        jdbi.useHandleUnchecked { handle ->
            handle.createUpdate(
                """
                    insert into users(id, email, password, type)
                    values(:id, :email, :password, :type)
                """.trimIndent()
            )
                .bind("id", clinicUserId)
                .bind("email", clinicEmail)
                .bind("password", UUID.randomUUID().toString())
                .bind("type", "CLINIC")
                .execute()
        }

        jdbi.useHandleUnchecked { handle ->
            handle.createUpdate(
                """
                    insert into clinics(id, user_id)
                    values(:id, :user_id)
                """.trimIndent()
            )
                .bind("id", clinicId)
                .bind("user_id", clinicUserId)
                .execute()
        }

        jdbi.useHandleUnchecked { handle ->
            handle.createUpdate(
                """
                    insert into users(id, email, password, type)
                    values(:id, :email, :password, :type)
                """.trimIndent()
            )
                .bind("id", clientUserId)
                .bind("email", "${UUID.randomUUID()}@test.test")
                .bind("password", UUID.randomUUID().toString())
                .bind("type", "CLIENT")
                .execute()
        }

        jdbi.useHandleUnchecked { handle ->
            handle.createUpdate(
                """
                    insert into clients(id, user_id, clinic_id, name, surname, phone)
                    values(:id, :user_id, :clinic_id ,:name, :surname, :phone)
                """.trimIndent()
            )
                .bind("id", clientId)
                .bind("user_id", clientUserId)
                .bind("clinic_id", clinicId)
                .bind("name", "Testname")
                .bind("surname", "Testsurname")
                .bind("phone", "666777888")
                .execute()
        }

        val token = JWT.create()
            .withAudience("supervet")
            .withIssuer("supervet")
            .withClaim("type", "CLINIC")
            .withClaim("user_id", clinicUserId.toString())
            .withClaim("email", clinicEmail)
            .sign(Algorithm.HMAC512("supervet"))

        val response = client.post("clinics/create-patient") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(createPatientPayload)
        }

        assertEquals(HttpStatusCode.Created, response.status)

        val createdPatient = jdbi.withHandleUnchecked { handle ->
            handle.createQuery(
                """
                select name, breed, age, weight, status, client_id
                from patients
                where client_id = :clientId
                """.trimIndent()
            )
                .bind("clientId", clientId)
                .map(MapMapper())
                .one()
        }

        assertNotNull(createdPatient)
        assertEquals(createPatientPayload["clientId"], createdPatient["client_id"].toString())
        assertEquals(createPatientPayload["name"], createdPatient["name"])
        assertEquals(createPatientPayload["breed"], createdPatient["breed"])
        assertEquals(createPatientPayload["age"], createdPatient["age"])
        assertEquals(createPatientPayload["weight"], createdPatient["weight"])
        assertEquals(createPatientPayload["status"], createdPatient["status"])
    }

    @Test
    fun `should not add a new patient if the client does not belong to the clinic`() =
        testApplicationWithDependencies { testRepository, jdbi, client, customConfig ->
            val clinicUserId = UUID.randomUUID()
            val clinicId = UUID.randomUUID()
            val clinicEmail = "${UUID.randomUUID()}@test.test"
            val otherClinicUserId = UUID.randomUUID()
            val otherClinicId = UUID.randomUUID()
            val clientUserId = UUID.randomUUID()
            val clientId = UUID.randomUUID()

            val createPatientPayload = mapOf(
                "clientId" to clientId.toString(),
                "name" to "Buddy",
                "breed" to "Golden Retriever",
                "age" to "3",
                "weight" to 25,
                "status" to "Healthy",
            )

            jdbi.useHandleUnchecked { handle ->
                handle.createUpdate(
                    """
                    insert into users(id, email, password, type)
                    values(:id, :email, :password, :type)
                """.trimIndent()
                )
                    .bind("id", clinicUserId)
                    .bind("email", clinicEmail)
                    .bind("password", UUID.randomUUID().toString())
                    .bind("type", "CLINIC")
                    .execute()
            }

            jdbi.useHandleUnchecked { handle ->
                handle.createUpdate(
                    """
                    insert into clinics(id, user_id)
                    values(:id, :user_id)
                """.trimIndent()
                )
                    .bind("id", clinicId)
                    .bind("user_id", clinicUserId)
                    .execute()
            }

            jdbi.useHandleUnchecked { handle ->
                handle.createUpdate(
                    """
                    insert into users(id, email, password, type)
                    values(:id, :email, :password, :type)
                """.trimIndent()
                )
                    .bind("id", otherClinicUserId)
                    .bind("email", "${UUID.randomUUID()}@test.test")
                    .bind("password", UUID.randomUUID().toString())
                    .bind("type", "CLINIC")
                    .execute()
            }

            jdbi.useHandleUnchecked { handle ->
                handle.createUpdate(
                    """
                    insert into clinics(id, user_id)
                    values(:id, :user_id)
                """.trimIndent()
                )
                    .bind("id", otherClinicId)
                    .bind("user_id", otherClinicUserId)
                    .execute()
            }

            jdbi.useHandleUnchecked { handle ->
                handle.createUpdate(
                    """
                    insert into users(id, email, password, type)
                    values(:id, :email, :password, :type)
                """.trimIndent()
                )
                    .bind("id", clientUserId)
                    .bind("email", "${UUID.randomUUID()}@test.test")
                    .bind("password", UUID.randomUUID().toString())
                    .bind("type", "CLIENT")
                    .execute()
            }

            jdbi.useHandleUnchecked { handle ->
                handle.createUpdate(
                    """
                    insert into clients(id, user_id, clinic_id, name, surname, phone)
                    values(:id, :user_id, :clinic_id ,:name, :surname, :phone)
                """.trimIndent()
                )
                    .bind("id", clientId)
                    .bind("user_id", clientUserId)
                    .bind("clinic_id", otherClinicId)
                    .bind("name", "Testname")
                    .bind("surname", "Testsurname")
                    .bind("phone", "666777888")
                    .execute()
            }

            val token = JWT.create()
                .withAudience("supervet")
                .withIssuer("supervet")
                .withClaim("type", "CLINIC")
                .withClaim("user_id", clinicUserId.toString())
                .withClaim("email", clinicEmail)
                .sign(Algorithm.HMAC512("supervet"))

            val response = client.post("clinics/create-patient") {
                bearerAuth(token)
                contentType(ContentType.Application.Json)
                setBody(createPatientPayload)
            }

            assertEquals(HttpStatusCode.Unauthorized, response.status)

            assertThrows<IllegalStateException> {
                jdbi.withHandleUnchecked { handle ->
                    handle.createQuery(
                        """
                select name, breed, age, weight, status, client_id
                from patients
                where client_id = :clientId
                """.trimIndent()
                    )
                        .bind("clientId", clientId)
                        .map(MapMapper())
                        .one()
                }
            }
        }
}