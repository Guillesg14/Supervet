package com.supervet.auth.sign_up

import at.favre.lib.crypto.bcrypt.BCrypt
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import java.util.*

class SignUpRepository(private val jdbi: Jdbi) {

   fun saveClinic(clinicSignUpRequest: ClinicSignUpRequest) {
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
   }

}