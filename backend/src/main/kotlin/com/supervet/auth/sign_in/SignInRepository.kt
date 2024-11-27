package com.supervet.auth.sign_in

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import java.util.*

class SignInRepository(private val jdbi: Jdbi) {
    fun getUserFrom(email: String) =
        jdbi.withHandleUnchecked { handle ->
            handle.createQuery(
                """
                select id, email, password, type
                from users u
                where email = :email
                """.trimIndent()
            )
                .bind("email", email)
                .map { rs, _ ->
                    User(
                        id = UUID.fromString(rs.getString("id")),
                        email = rs.getString("email"),
                        password = rs.getString("password"),
                        type = rs.getString("type")
                    )
                }
                .firstOrNull()
        }
}