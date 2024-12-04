package com.supervet.clinics

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.supervet.acceptance.helpers.testApplicationWithDependencies
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals


class GetClientsTest {
    @Test
    fun `should get clients list`() = testApplicationWithDependencies { testRepository, jdbi, client, customConfig ->
        val clinicUserId = UUID.randomUUID()
        val clinicEmail = "${UUID.randomUUID()}@test.test"
        val clinicId = UUID.randomUUID()
        val clientUserId = UUID.randomUUID()
        val clientId = UUID.randomUUID()

        jdbi.useHandleUnchecked { handle ->
            handle.createUpdate(
                """
            INSERT INTO users (id, email, password, type)
            VALUES (:id, :email, :password, :type);
            """.trimIndent()
            )
                .bind("id", clinicUserId)
                .bind("email", clinicEmail)
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
                .bind("email", "${UUID.randomUUID()}@test.test")
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
            .withClaim("type", "CLINIC")
            .withClaim("user_id", clinicId.toString())
            .withClaim("email", clinicEmail)
            .sign(Algorithm.HMAC512("supervet"))

        val response = client.get("/clinics/clients") {
            bearerAuth(token)
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val clientsResponse = response.body<List<Map<String, String>>>()

        assertEquals(1, clientsResponse.size)

        val client = clientsResponse.find { it["id"] == clientId.toString() }
        client shouldNotBe null
        client?.get("name") shouldBe "Test Name"
        client?.get("surname") shouldBe "Test Surname"
        client?.get("phone") shouldBe "123456789"
    }
}
