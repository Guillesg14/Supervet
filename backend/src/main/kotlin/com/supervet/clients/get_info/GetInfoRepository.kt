package com.supervet.clients.get_info

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import java.util.*

class GetInfoRepository(private val jdbi: Jdbi) {
    fun getClientDataByUserId(userId: UUID): Client =
        jdbi.inTransactionUnchecked { handle ->
            try {
                handle.createQuery(
                    """
                        SELECT id, name, surname, phone
                        FROM clients
                        WHERE user_id = :user_id
                    """.trimIndent()
                )
                    .bind("user_id", userId)
                    .map { rs, _ ->
                        Client(
                            id = UUID.fromString(rs.getString("id")),
                            name = rs.getString("name"),
                            surname = rs.getString("surname"),
                            phone = rs.getString("phone")
                        )
                    }
                    .one()
            } catch (e: Exception) {
                when (e) {
                    is IllegalStateException -> throw ClientDoesNotExistException(userId)
                    else -> throw e
                }
            }
        }
}

