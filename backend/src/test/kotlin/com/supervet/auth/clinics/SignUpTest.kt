package com.supervet.auth.clinics

import at.favre.lib.crypto.bcrypt.BCrypt
import com.supervet.acceptance.helpers.testApplicationWithDependencies
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.Conflict
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SignUpTest {
    @Test
    fun `should register a clinic`() = testApplicationWithDependencies { jdbi, client, customConfig ->
        val signUpPayload = object {
            val email = "${UUID.randomUUID()}@test.test"
            val password = UUID.randomUUID().toString()
        }

        val response = client.post("auth/clinics/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(signUpPayload)
        }

        assertEquals(HttpStatusCode.Created, response.status)

        val createdUser = jdbi.withHandleUnchecked { handle ->
            handle.createQuery(
                """
                    select *
                    from users
                    where email = :email
                """.trimIndent()
            )
                .bind("email", signUpPayload.email)
                .map { rs, _ ->
                    object {
                        val password = rs.getString("password")
                    }
                }
                .one()
        }

        assertTrue { BCrypt.verifyer().verify(signUpPayload.password.toCharArray(), createdUser.password).verified }
    }

    @Test
    fun `should not allow duplicate clinic registration`() =
        testApplicationWithDependencies { jdbi, client, customConfig ->
            val signUpPayload = object {
                val email = "${UUID.randomUUID()}@test.test"
                val password = UUID.randomUUID().toString()
            }

            client.post("auth/clinics/sign-up") {
                contentType(ContentType.Application.Json)
                setBody(signUpPayload)
            }

            val duplicateResponse = client.post("auth/clinics/sign-up") {
                contentType(ContentType.Application.Json)
                setBody(signUpPayload)
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
                    .bind("email", signUpPayload.email)
                    .mapTo(Int::class.java)
                    .one()
            }

            assertEquals(1, userCount)
        }

}