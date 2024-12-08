package com.supervet.clients

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.supervet.acceptance.helpers.testApplicationWithDependencies
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class GetClinicInfoTest {
    @Test
    fun `should get clinic info`() = testApplicationWithDependencies { testRepository, jdbi, httpClient, customConfig ->
        val clinic = testRepository.createClinic()
        val client = testRepository.createClient(clinic)

        val token = JWT.create()
            .withAudience("supervet")
            .withIssuer("supervet")
            .withClaim("type", "CLIENT")
            .withClaim("user_id", client.userId.toString())
            .withClaim("email", client.email)
            .sign(Algorithm.HMAC512("supervet"))

        val response = httpClient.get("/clients/clinic-info") {
            bearerAuth(token)
        }
        assertEquals(HttpStatusCode.OK, response.status)

        val clinicFromDb = jdbi.withHandleUnchecked { handle ->
            handle.createQuery(
                """
                SELECT id, phone, address
                FROM clinics
                WHERE id = :clinic_id                    
                """.trimIndent()
            )
                .bind("clinic_id", clinic.id)
                .map { rs, _ ->
                    mapOf(
                        "id" to UUID.fromString(rs.getString("id")),
                        "phone" to rs.getInt("phone"),
                        "address" to rs.getString("address")
                        )
                }
                .one()
        }
        val clinicResponse = response.body<Map<String, String>>()
        assertEquals(clinicFromDb["id"].toString(), clinicResponse["id"].toString())
        assertEquals(clinicFromDb["phone"].toString(), clinicResponse["phone"])
        assertEquals(clinicFromDb["address"], clinicResponse["address"])
        }

}