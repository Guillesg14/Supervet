package com.supervet.clients.show_data

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import java.util.*

class ShowClientDataRepository(private val jdbi: Jdbi) {
    fun getClientDataByUserId(userId: String): List<Client> {
        return jdbi.inTransactionUnchecked { handle ->
            handle.createQuery(
                """
                SELECT name, surname, phone, user_id
                FROM clients
                WHERE user_id = :user_id
                """.trimIndent()
            )
                .bind("user_id", UUID.fromString(userId))
                .map { rs, _ ->
                    Client(
                        user_id = UUID.fromString(rs.getString("user_id")),
                        name = rs.getString("name"),
                        surname = rs.getString("surname"),
                        phone = rs.getString("phone")
                    )
                }
                .list()
        }
    }
}

