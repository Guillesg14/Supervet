package com.supervet.clinics

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.supervet.acceptance.helpers.testApplicationWithDependencies
import io.ktor.client.request.*
import io.ktor.http.*
import org.jdbi.v3.core.kotlin.mapTo
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class DeleteClientTest {
    @Test
    fun `should remove a client`() = testApplicationWithDependencies { testRepository, jdbi, httpClient, customConfig ->
        val clinic = testRepository.createClinic()
        val client = testRepository.createClient(clinic)

        val token = JWT.create()
            .withAudience("supervet")
            .withIssuer("supervet")
            .withClaim("type", "CLINIC")
            .withClaim("user_id", clinic.userId.toString())
            .withClaim("email", clinic.email)
            .sign(Algorithm.HMAC512("supervet"))

        val response = httpClient.delete("clinics/delete-client/${client.id}") {
            bearerAuth(token)
        }

        assertEquals(HttpStatusCode.NoContent, response.status)

        assertThrows<IllegalStateException> {
            jdbi.withHandleUnchecked { handle ->
                handle.createQuery(
                    """
                    select 1
                    from clients
                    where user_id = :user_id
                """.trimIndent()
                )
                    .bind("user_id", client.id)
                    .mapTo<Boolean>()
                    .one()
            }
        }

        assertThrows<IllegalStateException> {
            jdbi.withHandleUnchecked { handle ->
                handle.createQuery(
                    """
                    select 1
                    from users
                    where id = :user_id
                """.trimIndent()
                )
                    .bind("user_id", client.id)
                    .mapTo<Boolean>()
                    .one()
            }
        }
    }

    @Test
    fun `should not remove a client if the client does not belong to the clinic`() =
        testApplicationWithDependencies { testRepository, jdbi, httpClient, customConfig ->
            val clinic = testRepository.createClinic()
            val otherClinic = testRepository.createClinic()
            val client = testRepository.createClient(otherClinic)

            val token = JWT.create()
                .withAudience("supervet")
                .withIssuer("supervet")
                .withClaim("type", "CLINIC")
                .withClaim("user_id", clinic.userId.toString())
                .withClaim("email", clinic.email)
                .sign(Algorithm.HMAC512("supervet"))

            val response = httpClient.delete("clinics/delete-client/${client.id}") {
                bearerAuth(token)
            }

            assertEquals(HttpStatusCode.Unauthorized, response.status)

            assertDoesNotThrow {
                jdbi.withHandleUnchecked { handle ->
                    handle.createQuery(
                        """
                    select 1
                    from clients
                    where id = :id
                """.trimIndent()
                    )
                        .bind("id", client.id)
                        .mapTo<Boolean>()
                        .one()
                }
            }

            assertDoesNotThrow {
                jdbi.withHandleUnchecked { handle ->
                    handle.createQuery(
                        """
                    select 1
                    from users
                    where id = :user_id
                """.trimIndent()
                    )
                        .bind("user_id", client.userId)
                        .mapTo<Boolean>()
                        .one()
                }
            }
        }
}