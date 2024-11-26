package com.supervet.auth.clients

import at.favre.lib.crypto.bcrypt.BCrypt
import kotlin.random.Random
import com.supervet.acceptance.helpers.testApplicationWithDependencies
import io.ktor.client.request.*
import io.ktor.http.*
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ClientSignUpTest {
    @Test
    fun `should register a client`() = testApplicationWithDependencies { jdbi, client, customConfig ->
        val addClientPayload = mapOf(
            "clinicId" to UUID.randomUUID().toString(),
            "name" to UUID.randomUUID().toString(),
            "surname" to UUID.randomUUID().toString(),
            "phone" to Random.nextInt(100_000_000, 1_000_000_000),
            "email" to "${UUID.randomUUID()}@test.test",
            "password"  to UUID.randomUUID().toString()
        )

        val response = client.post("auth/clients/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(addClientPayload)
        }

        assertEquals(HttpStatusCode.Created, response.status)

        val createdClient = jdbi.withHandleUnchecked { handle ->
            handle.createQuery(
                """
                    select *
                    from clients
                    where email = :email
                """.trimIndent()
            )
                .bind("email", addClientPayload["email"])
                .map { rs, _ ->
                    object {
                        val password = rs.getString("password")
                    }
                }
                .one()
        }

        assertTrue { BCrypt.verifyer().verify((addClientPayload["password"] as String ).toCharArray(), createdClient.password).verified}
    }


    @Test
    fun `should not allow duplicate clinic registration`() =
        testApplicationWithDependencies { jdbi, client, customConfig ->

            val addClientPayload = mapOf(
                "clinicId" to UUID.randomUUID().toString(),
                "name" to UUID.randomUUID().toString(),
                "surname" to UUID.randomUUID().toString(),
                "phone" to Random.nextInt(100_000_000, 1_000_000_000),
                "email" to "${UUID.randomUUID()}@test.test",
                "password"  to UUID.randomUUID().toString()
            )

            client.post("auth/clients/sign-up") {
                contentType(ContentType.Application.Json)
                setBody(addClientPayload)
            }

            val duplicateResponse = client.post("auth/clients/sign-up") {
                contentType(ContentType.Application.Json)
                setBody(addClientPayload)
            }

            assertEquals(HttpStatusCode.Conflict, duplicateResponse.status)

            val userCount = jdbi.withHandleUnchecked { handle ->
                handle.createQuery(
                    """
                    select count(*)
                    from clients
                    where email = :email
                """.trimIndent()
                )
                    .bind("email", addClientPayload["email"] as String)
                    .mapTo(Int::class.java)
                    .one()
            }

            assertEquals(1, userCount)
        }

}