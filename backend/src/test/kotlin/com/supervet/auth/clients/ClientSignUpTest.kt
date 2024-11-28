package com.supervet.auth.clients;

import at.favre.lib.crypto.bcrypt.BCrypt
import kotlin.random.Random
import com.supervet.acceptance.helpers.testApplicationWithDependencies
import io.kotest.matchers.shouldBe
import io.ktor.client.request.*
import io.ktor.http.*
import org.jdbi.v3.core.kotlin.mapTo
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.junit.jupiter.api.assertDoesNotThrow
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ClientSignUpTest {
    @Test
    fun `should register a client`() = testApplicationWithDependencies { jdbi, client, customConfig ->
        val clientSignUpPayload = mapOf(
            "clinicId" to UUID.randomUUID().toString(),
            "email" to "${UUID.randomUUID()}@test.test",
            "password" to UUID.randomUUID().toString(),
            "name" to UUID.randomUUID().toString(),
            "surname" to UUID.randomUUID().toString(),
            "phone" to Random.nextInt(100_000_000, 1_000_000_000)
        )

        jdbi.useHandleUnchecked { handle ->
            handle.createUpdate(
                """
                    insert into users(id, email, password, type)
                    values(:id, :email, :password, :type)
                """.trimIndent()
            )
                .bind("id", UUID.fromString(clientSignUpPayload["clinicId"].toString()))
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
                .bind("id", UUID.randomUUID())
                .bind("user_id", UUID.fromString(clientSignUpPayload["clinicId"].toString()))
                .execute()
        }

        val response = client.post("auth/clients/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(clientSignUpPayload)
        }

        assertEquals(HttpStatusCode.Created, response.status)

        val createdClient = jdbi.withHandleUnchecked { handle ->
            handle.createQuery(
                """
                    select *
                    from users
                    where email = :email
                """.trimIndent()
            )
                .bind("email", clientSignUpPayload["email"])
                .map { rs, _ ->
                    object {
                        val id = UUID.fromString(rs.getString("id"))
                        val password = rs.getString("password")
                        val type = rs.getString("type")
                    }
                }
                .one()
        }

        assertTrue {
            BCrypt.verifyer()
                .verify((clientSignUpPayload["password"] as String).toCharArray(), createdClient.password).verified
        }
        assertEquals("CLIENT", createdClient.type)

        assertDoesNotThrow {
            jdbi.withHandleUnchecked { handle ->
                handle.createQuery(
                    """
                    select 1
                    from clients
                    where user_id = :user_id
                """.trimIndent()
                )
                    .bind("user_id", createdClient.id)
                    .mapTo<Boolean>()
                    .one()
            }
        }
    }

    @Test
    fun `should not allow duplicate client registration`() =
        testApplicationWithDependencies { jdbi, client, customConfig ->
            val clientSignUpPayload = mapOf(
                "clinicId" to UUID.randomUUID().toString(),
                "email" to "${UUID.randomUUID()}@test.test",
                "password" to UUID.randomUUID().toString(),
                "name" to UUID.randomUUID().toString(),
                "surname" to UUID.randomUUID().toString(),
                "phone" to Random.nextInt(100_000_000, 1_000_000_000)
            )

            jdbi.useHandleUnchecked { handle ->
                handle.createUpdate(
                    """
                    insert into users(id, email, password, type)
                    values(:id, :email, :password, :type)
                """.trimIndent()
                )
                    .bind("id", UUID.fromString(clientSignUpPayload["clinicId"].toString()))
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
                    .bind("id", UUID.randomUUID())
                    .bind("user_id", UUID.fromString(clientSignUpPayload["clinicId"].toString()))
                    .execute()
            }

            client.post("auth/clients/sign-up") {
                contentType(ContentType.Application.Json)
                setBody(clientSignUpPayload)
            }

            val duplicateResponse = client.post("auth/clients/sign-up") {
                contentType(ContentType.Application.Json)
                setBody(clientSignUpPayload)
            }

            assertEquals(HttpStatusCode.Conflict, duplicateResponse.status)

            val userId = jdbi.withHandleUnchecked { handle ->
                handle.createQuery(
                    """
                    select id
                    from users
                    where email = :email
                """.trimIndent()
                )
                    .bind("email", clientSignUpPayload["email"] as String)
                    .mapTo(UUID::class.java)
                    .one()
            }

            val clientsCount = jdbi.withHandleUnchecked { handle ->
                handle.createQuery(
                    """
                        select count(1)
                        from clients
                        where user_id = :user_id
                    """.trimIndent()
                )
                    .bind("user_id", userId)
                    .mapTo<Int>()
                    .one()
            }

            clientsCount shouldBe 1
        }

    @Test
    fun `should return not found if the clinic does not exist`() =
        testApplicationWithDependencies { jdbi, client, customConfig ->
            val clientSignUpPayload = mapOf(
                "clinicId" to UUID.randomUUID().toString(),
                "name" to UUID.randomUUID().toString(),
                "surname" to UUID.randomUUID().toString(),
                "phone" to Random.nextInt(100_000_000, 1_000_000_000),
                "email" to "${UUID.randomUUID()}@test.test",
                "password" to UUID.randomUUID().toString()
            )

            val response = client.post("auth/clients/sign-up") {
                contentType(ContentType.Application.Json)
                setBody(clientSignUpPayload)
            }

            assertEquals(HttpStatusCode.NotFound, response.status)
        }
}