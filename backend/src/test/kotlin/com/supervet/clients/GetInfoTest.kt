package com.supervet.clients

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.supervet.acceptance.helpers.testApplicationWithDependencies
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals


class GetInfoTest {
    @Test
    fun `should get client info`() = testApplicationWithDependencies { testRepository, jdbi, client, customConfig ->
        val clinicUserId = UUID.randomUUID()
        val clinicId = UUID.randomUUID()
        val clientUserId = UUID.randomUUID()
        val clientId = UUID.randomUUID()
        val clientEmail = "${UUID.randomUUID()}@test.test"

        jdbi.useHandleUnchecked { handle ->
            handle.createUpdate(
                """
            INSERT INTO users (id, email, password, type)
            VALUES (:id, :email, :password, :type);
            """.trimIndent()
            )
                .bind("id", clinicUserId)
                .bind("email", "clinic@test.com")
                .bind("password", "hashed_password")
                .bind("type", "CLINIC")
                .execute()
        }

        jdbi.useHandleUnchecked { handle ->
            handle.createUpdate(
                """
            INSERT INTO clinics (id, user_id)
            VALUES (:id, :user_id);
            """.trimIndent()
            )
                .bind("id", clinicId)
                .bind("user_id", clinicUserId)
                .execute()
        }

        jdbi.useHandleUnchecked { handle ->
            handle.createUpdate(
                """
            INSERT INTO users (id, email, password, type)
            VALUES (:id, :email, :password, :type);
            """.trimIndent()
            )
                .bind("id", clientUserId)
                .bind("email", clientEmail)
                .bind("password", "hashed_password")
                .bind("type", "CLIENT")
                .execute()
        }

        jdbi.useHandleUnchecked { handle ->
            handle.createUpdate(
                """
            INSERT INTO clients (id, user_id, clinic_id, name, surname, phone)
            VALUES (:id, :user_id, :clinic_id, :name, :surname, :phone);
            """.trimIndent()
            )
                .bind("id", clientId)
                .bind("user_id", clientUserId)
                .bind("clinic_id", clinicId)
                .bind("name", "Test Name")
                .bind("surname", "Test Surname")
                .bind("phone", "123456789")
                .execute()
        }

        val token = JWT.create()
            .withAudience("supervet")
            .withIssuer("supervet")
            .withClaim("type", "CLIENT")
            .withClaim("user_id", clientUserId.toString())
            .withClaim("email", clientEmail)
            .sign(Algorithm.HMAC512("supervet"))

        val response = client.get("/clients/info") {
            bearerAuth(token)
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val clientFromDb = jdbi.withHandleUnchecked { handle ->
            handle.createQuery(
                """
            SELECT id, name, surname, phone
            FROM clients
            WHERE user_id = :user_id
            """.trimIndent()
            )
                .bind("user_id", clientUserId)
                .map { rs, _ ->
                    mapOf(
                        "id" to UUID.fromString(rs.getString("id")),
                        "name" to rs.getString("name"),
                        "surname" to rs.getString("surname"),
                        "phone" to rs.getString("phone")
                    )
                }
                .one()
        }
        val clientsResponse = response.body<Map<String, String>>()
        assertEquals(clientFromDb["id"].toString(), clientsResponse["id"])
        assertEquals(clientFromDb["name"], clientsResponse["name"])
        assertEquals(clientFromDb["surname"], clientsResponse["surname"])
        assertEquals(clientFromDb["phone"], clientsResponse["phone"])
    }
}
