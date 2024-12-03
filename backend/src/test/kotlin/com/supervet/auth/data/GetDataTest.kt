package com.supervet.auth.data

import com.supervet.acceptance.helpers.testApplicationWithDependencies
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import org.jdbi.v3.core.kotlin.withHandleUnchecked


class GetDataTest {
    @Test
    fun `should get clients data`() = testApplicationWithDependencies { jdbi, client, customConfig ->
        // 1. Creamos un usuario para la clínica
        val clinicUserId = UUID.randomUUID()
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

        // 2. Insertamos la clínica asociada al usuario
        val clinicId = UUID.randomUUID()
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

        // 3. Creamos un usuario para el cliente
        val clientUserId = UUID.randomUUID()
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

        // 4. Insertamos un cliente asociado al usuario y la clínica
        val clientId = UUID.randomUUID()
        jdbi.useHandleUnchecked { handle ->
            handle.createUpdate(
                """
            INSERT INTO clients (id, user_id, clinic_id, name, surname, phone)
            VALUES (:id, :user_id, :clinic_id, :name, :surname, :phone);
            """.trimIndent()
            )
                .bind("id", clientId)
                .bind("user_id", clientUserId)
                .bind("clinic_id", clinicUserId)
                .bind("name", "Test Name")
                .bind("surname", "Test Surname")
                .bind("phone", "123456789")
                .execute()
        }

        // 5. Realizamos la petición al endpoint
        val response = client.post("/auth/data/show_clients") {
            contentType(ContentType.Application.Json)
            setBody("""{"clinicId": "$clinicUserId"}""")
        }

        // 6. Comprobamos la respuesta
        assertEquals(HttpStatusCode.OK, response.status)

        val clientsFromDb = jdbi.withHandleUnchecked { handle ->
            handle.createQuery(
                """
            SELECT name, surname, phone
            FROM clients
            WHERE clinic_id = :clinic_id
            """.trimIndent()
            )
                .bind("clinic_id", clinicUserId)
                .map { rs, _ ->
                    mapOf(
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
        }
    }
}
