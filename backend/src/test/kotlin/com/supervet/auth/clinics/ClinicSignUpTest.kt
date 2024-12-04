package com.supervet.auth.clinics

import at.favre.lib.crypto.bcrypt.BCrypt
import com.supervet.acceptance.helpers.testApplicationWithDependencies
import com.supervet.auth.clinics.sign_up.ClinicSignUpRequest
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.Created
import org.jdbi.v3.core.kotlin.mapTo
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.junit.jupiter.api.assertDoesNotThrow
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ClinicSignUpTest {
    @Test
    fun `should register a clinic`() = testApplicationWithDependencies { testRepository, jdbi, httpClient, customConfig ->
        val clinicSignUpPayload = ClinicSignUpRequest(
            email = "${UUID.randomUUID()}@test.test",
            password = UUID.randomUUID().toString()
        )

        val response = httpClient.post("auth/clinics/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(clinicSignUpPayload)
        }

        assertEquals(Created, response.status)

        val createdClinic = jdbi.withHandleUnchecked { handle ->
            handle.createQuery(
                """
                    select *
                    from users
                    where email = :email
                """.trimIndent()
            )
                .bind("email", clinicSignUpPayload.email)
                .map { rs, _ ->
                    object {
                        val id = UUID.fromString(rs.getString("id"))
                        val password = rs.getString("password")
                        val type = rs.getString("type")
                    }
                }
                .one()
        }

        assertTrue { BCrypt.verifyer().verify(clinicSignUpPayload.password.toCharArray(), createdClinic.password).verified }
        assertEquals("CLINIC", createdClinic.type)

        assertDoesNotThrow {
            jdbi.withHandleUnchecked { handle ->
                handle.createQuery(
                    """
                    select 1
                    from clinics
                    where user_id = :user_id
                """.trimIndent()
                )
                    .bind("user_id", createdClinic.id)
                    .mapTo<Boolean>()
                    .one()
            }
        }
    }

    @Test
    fun `should not allow duplicate clinic registration`() =
        testApplicationWithDependencies { testRepository, jdbi, httpClient, customConfig ->
            val clinicSignUpPayload = ClinicSignUpRequest(
                email = "${UUID.randomUUID()}@test.test",
                password = UUID.randomUUID().toString()
            )

            val response = httpClient.post("auth/clinics/sign-up") {
                contentType(ContentType.Application.Json)
                setBody(clinicSignUpPayload)
            }

            assertEquals(Created, response.status)

            val duplicateResponse = httpClient.post("auth/clinics/sign-up") {
                contentType(ContentType.Application.Json)
                setBody(clinicSignUpPayload)
            }

            assertEquals(Conflict, duplicateResponse.status)

            val userCount = jdbi.withHandleUnchecked { handle ->
                handle.createQuery(
                    """
                    select count(*)
                    from users
                    where email = :email
                """.trimIndent()
                )
                    .bind("email", clinicSignUpPayload.email)
                    .mapTo(Int::class.java)
                    .one()
            }

            assertEquals(1, userCount)
        }
}