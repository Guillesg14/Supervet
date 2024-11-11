package com.supervet

import at.favre.lib.crypto.bcrypt.BCrypt
import com.supervet.acceptance.helpers.testApplicationWithDependencies
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.OK
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import java.util.*
import kotlin.test.*

class ClinicRegistrationTest {
    @Test
    fun `should register a clinic`() = testApplicationWithDependencies { jdbi, client, customConfig ->
        val signUpPayload = object {
            val email = "${UUID.randomUUID()}@test.test"
            val password = UUID.randomUUID().toString()
        }

        val response = client.post("/clinics/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(signUpPayload)
        }

        assertEquals(Created, response.status)

        val createdUser = jdbi.withHandleUnchecked { handle ->
            handle.createQuery(
                """
                    select *
                    from users
                    where email = :email
                """.trimIndent()
            )
                .bind("email", signUpPayload.email)
                .map { rs, _ ->
                    object {
                        val password = rs.getString("password")
                    }
                }
                .one()
        }

        assertTrue { BCrypt.verifyer().verify(signUpPayload.password.toCharArray(), createdUser.password).verified }
    }
}
