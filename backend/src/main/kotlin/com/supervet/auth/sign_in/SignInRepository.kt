package com.supervet.auth.sign_in

import at.favre.lib.crypto.bcrypt.BCrypt
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import java.util.*

class SignInRepository (private val jdbi: Jdbi) {

    fun logClinic(clinicSignInRequest: ClinicSignInRequest): User {
            val existingUser = jdbi.withHandleUnchecked { handle ->
                handle.createQuery(
                    """
                    select id, email, password
                    from users
                    where email = :email
                    """.trimIndent()
                )
                    .bind("email", clinicSignInRequest.email)
                    .map { rs, _ ->
                        User(
                            id = UUID.fromString(rs.getString("id")),
                            email = rs.getString("email"),
                            password = rs.getString("password")
                        )
                    }
                    .one()
            }
            if (!BCrypt.verifyer().verify(clinicSignInRequest.password.toCharArray(), existingUser.password).verified) {
               throw WrongPassword()
            }
        return existingUser
    }
}
class WrongPassword() : Exception("Wrong Password, try again")
data class User(val id: UUID, val email: String, val password: String)
data class ClinicSignInResponse(val token: String)