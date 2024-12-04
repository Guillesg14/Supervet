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
    fun `should remove a client`() = testApplicationWithDependencies { jdbi, client, customConfig ->
        val clinicUserId = UUID.randomUUID()
        val clinicEmail = "${UUID.randomUUID()}@test.test"
        val clinicId = UUID.randomUUID()
        val clientUserId = UUID.randomUUID()
        val clientId = UUID.randomUUID()

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

        val response = client.delete("clinics/delete-client/$clientId")  {
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
                    .bind("user_id", clientId)
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
                    .bind("user_id", clientId)
                    .mapTo<Boolean>()
                    .one()
            }
        }
    }

    @Test
    fun `should not remove a client if the client does not belong to the clinic`() = testApplicationWithDependencies { jdbi, client, customConfig ->
        val clinicIntruderUserId = UUID.randomUUID()
        val clinicIntruderId = UUID.randomUUID()
        val clinicIntruderEmail = "${UUID.randomUUID()}@test.test"
        val clinicOwnerUserId = UUID.randomUUID()
        val clinicOwnerId = UUID.randomUUID()
        val clientId = UUID.randomUUID()

        jdbi.useHandleUnchecked { handle ->
            handle.createUpdate(
                """
                    insert into users(id, email, password, type)
                    values(:id, :email, :password, :type)
                """.trimIndent()
            )
                .bind("id", clinicOwnerUserId)
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
                .bind("id", clinicOwnerId)
                .bind("user_id", clinicOwnerUserId)
                .execute()
        }

        jdbi.useHandleUnchecked { handle ->
            handle.createUpdate(
                """
                    insert into users(id, email, password, type)
                    values(:id, :email, :password, :type)
                """.trimIndent()
            )
                .bind("id", clinicIntruderUserId)
                .bind("email", clinicIntruderEmail)
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
                .bind("id", clinicIntruderId)
                .bind("user_id", clinicIntruderUserId)
                .execute()
        }

        jdbi.useHandleUnchecked { handle ->
            handle.createUpdate(
                """
                    insert into users(id, email, password, type)
                    values(:id, :email, :password, :type)
                """.trimIndent()
            )
                .bind("id", clientId)
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
                .bind("id", UUID.randomUUID())
                .bind("user_id", clientId)
                .bind("clinic_id", clinicOwnerId)
                .bind("name", "Testname")
                .bind("surname", "Testsurname")
                .bind("phone", "666777888")
                .execute()
        }

        val token = JWT.create()
            .withAudience("supervet")
            .withIssuer("supervet")
            .withClaim("type", "CLINIC")
            .withClaim("user_id", clinicIntruderUserId.toString())
            .withClaim("email", clinicIntruderEmail)
            .sign(Algorithm.HMAC512("supervet"))

        val response = client.delete("clinics/delete-client/$clientId")  {
            bearerAuth(token)
        }

        assertEquals(HttpStatusCode.NoContent, response.status)

        assertDoesNotThrow {
            jdbi.withHandleUnchecked { handle ->
                handle.createQuery(
                    """
                    select 1
                    from clients
                    where user_id = :user_id
                """.trimIndent()
                )
                    .bind("user_id", clientId)
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
                    .bind("user_id", clientId)
                    .mapTo<Boolean>()
                    .one()
            }
        }
    }
}