package com.supervet.auth.clinics.sign_up

import at.favre.lib.crypto.bcrypt.BCrypt
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.useTransactionUnchecked
import org.postgresql.util.PSQLException
import java.util.*

private const val UNIQUE_VIOLATION = "23505"

class SignUpRepository(private val jdbi: Jdbi) {

    fun saveClinic(clinicSignUpRequest: ClinicSignUpRequest) {
        try {
            jdbi.useTransactionUnchecked { handle ->
                val userId = UUID.randomUUID()
                handle.createUpdate(
                    """
                    insert into users(id, email, password, type)
                    values(:id, :email, :password, :type)
                """.trimIndent()
                )
                    .bind("id", userId)
                    .bind("email", clinicSignUpRequest.email)
                    .bind(
                        "password",
                        BCrypt.withDefaults().hashToString(12, clinicSignUpRequest.password.toCharArray())
                    )
                    .bind("type", "CLINIC")
                    .execute()

                handle.createUpdate(
                    """
                        insert into clinics(id, user_id)
                        values (:id, :user_id)
                    """.trimIndent()
                )
                    .bind("id", UUID.randomUUID())
                    .bind("user_id", userId)
                    .execute()
            }
        } catch (e: Exception) {
            val cause = e.cause
            if (cause is PSQLException && cause.sqlState == UNIQUE_VIOLATION) {
                throw UserAlreadyExistsException(clinicSignUpRequest.email)
            } else {
                throw e
            }
        }
    }
}