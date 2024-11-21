package com.supervet.auth.sign_up

import at.favre.lib.crypto.bcrypt.BCrypt
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import org.postgresql.util.PSQLException
import java.util.*

private const val UNIQUE_VIOLATION = "23505"

class SignUpRepository(private val jdbi: Jdbi) {

   fun saveClinic(clinicSignUpRequest: ClinicSignUpRequest) {

       try {
           jdbi.useHandleUnchecked { handle ->
               handle.createUpdate(
                   """
                    insert into users(id, email, password, created_at)
                    values(:id, :email, :password, now())
                """.trimIndent()
               )
                   .bind("id", UUID.randomUUID())
                   .bind("email", clinicSignUpRequest.email)
                   .bind("password", BCrypt.withDefaults().hashToString(12, clinicSignUpRequest.password.toCharArray()))
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
class UserAlreadyExistsException(email: String) : Exception("User with email $email already exists.")