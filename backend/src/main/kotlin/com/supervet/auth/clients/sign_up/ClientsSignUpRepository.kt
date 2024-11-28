package com.supervet.auth.clients.sign_up

import at.favre.lib.crypto.bcrypt.BCrypt
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import org.postgresql.util.PSQLException
import java.util.*

private const val UNIQUE_VIOLATION = "23505"
private const val FOREIGN_KEY_EXCEPTION = "23503"

class AddClientRepository(private val jdbi: Jdbi) {

    fun saveClient(clientSignUpRequest: ClientSignUpRequest) {
        try {
            jdbi.inTransactionUnchecked { handle ->
                val userId = UUID.randomUUID()
                handle.createUpdate(
                    """
                    insert into users(id, email, password, type)
                    values(:id, :email, :password, :type)
                """.trimIndent()
                )
                    .bind("id", userId)
                    .bind("email", clientSignUpRequest.email)
                    .bind(
                        "password",
                        BCrypt.withDefaults().hashToString(12, clientSignUpRequest.password.toCharArray())
                    )
                    .bind("type", "CLIENT")
                    .execute()

                handle.createUpdate(
                    """
                    insert into clients(id, user_id, clinic_id, name, surname, phone)
                    values(:id, :user_id, :clinic_id ,:name, :surname, :phone)
                """.trimIndent()
                )
                    .bind("id", UUID.randomUUID())
                    .bind("user_id", userId)
                    .bind("clinic_id", UUID.fromString(clientSignUpRequest.clinicId))
                    .bind("name", clientSignUpRequest.name)
                    .bind("surname", clientSignUpRequest.surname)
                    .bind("phone", clientSignUpRequest.phone)
                    .execute()
            }
        } catch (e: Exception) {
            val cause = e.cause as PSQLException

            when (cause.sqlState) {
                UNIQUE_VIOLATION -> throw ClientAlreadyExistsException(clientSignUpRequest.email)
                FOREIGN_KEY_EXCEPTION -> throw ClinicDoesNotExistException(clientSignUpRequest.clinicId)
                else -> throw e
            }
        }
    }
}