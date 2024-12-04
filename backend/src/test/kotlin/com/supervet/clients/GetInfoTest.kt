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
    fun `should get client info`() = testApplicationWithDependencies { testRepository, jdbi, httpClient, customConfig ->
        val clinic = testRepository.createClinic()
        val client = testRepository.createClient(clinic)

        val token = JWT.create()
            .withAudience("supervet")
            .withIssuer("supervet")
            .withClaim("type", "CLIENT")
            .withClaim("user_id", client.userId.toString())
            .withClaim("email", client.email)
            .sign(Algorithm.HMAC512("supervet"))

        val response = httpClient.get("/clients/info") {
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
                .bind("user_id", client.userId)
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
