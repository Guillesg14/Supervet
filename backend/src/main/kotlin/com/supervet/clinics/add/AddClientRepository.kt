package com.supervet.clinics.add

import com.supervet.auth.sign_up.ClinicSignUpRequest
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import org.postgresql.util.PSQLException
import java.util.*

private const val UNIQUE_VIOLATION = "23505"

class AddClientRepository(private val jdbi: Jdbi) {

    fun saveClient(addClientRequest: AddClientRequest) {

        try {
            jdbi.useHandleUnchecked { handle ->
                handle.createUpdate(
                    """
                    insert into clients(id, clinicId, name, surname, phone, email, created_at)
                    values(:id, :clinicId ,:name, :surname, :phone, :email, now())
                """.trimIndent()
                )
                    .bind("id", UUID.randomUUID())
                    .bind("clinicId", addClientRequest.clinicId)
                    .bind("name", addClientRequest.name)
                    .bind("surname", addClientRequest.surname)
                    .bind("phone",addClientRequest.phone)
                    .bind("email", addClientRequest.email)
                    .execute()
            }
        } catch (e: Exception) {
            val cause = e.cause
            if (cause is PSQLException && cause.sqlState == UNIQUE_VIOLATION) {
                throw ClientAlreadyExistsException(addClientRequest.email)
            } else {
                throw e
            }
        }
    }
}
class ClientAlreadyExistsException(email: String) : Exception("client with email $email already exists.")