package com.supervet.auth.data



import com.supervet.acceptance.helpers.testApplicationWithDependencies
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals


class ClientGetGataTest {
    @Test
    fun `should get a client data`() = testApplicationWithDependencies { jdbi, client, customConfig ->
        val clinicUserId = UUID.randomUUID()
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
                .bind("email", "client@test.com")
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

        val response = client.post("/auth/data/show_client_data") {
            contentType(ContentType.Application.Json)
            setBody("""{"userId": "$clientUserId"}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val clientsFromDb = jdbi.withHandleUnchecked { handle ->
            handle.createQuery(
                """
            SELECT name, surname, phone, user_id
            FROM clients
            WHERE user_id = :user_id
            """.trimIndent()
            )
                .bind("user_id", clientUserId)
                .map { rs, _ ->
                    mapOf(
                        "user_id" to UUID.fromString(rs.getString("user_id")),
                        "name" to rs.getString("name"),
                        "surname" to rs.getString("surname"),
                        "phone" to rs.getString("phone")
                    )
                }
                .list()
        }
        val clientsResponse = response.body<List<Map<String, String>>>()
        assertEquals(clientsFromDb.size, clientsResponse.size)
        clientsFromDb.forEachIndexed { index, clientFromDb ->
            val clientInResponse = clientsResponse[index]
            assertEquals(clientFromDb["name"], clientInResponse["name"])
            assertEquals(clientFromDb["surname"], clientInResponse["surname"])
            assertEquals(clientFromDb["phone"], clientInResponse["phone"])
            assertEquals(clientFromDb["user_id"], clientUserId)
        }
    }
}
