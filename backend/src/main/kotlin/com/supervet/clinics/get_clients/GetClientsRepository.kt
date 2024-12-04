package com.supervet.clinics.get_clients

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import java.util.*

class GetClientsRepository(private val jdbi: Jdbi) {
    fun getClientsByClinicId(clinicUserId: UUID): List<Client> {
        return jdbi.inTransactionUnchecked { handle ->
            handle.createQuery(
                """
                SELECT id, name, surname, phone
                FROM clients
                WHERE clinic_id = (
                    SELECT c.id
                    FROM clinics c
                    JOIN users u on u.id = c.user_id
                    WHERE u.id = :clinicUserId
                )
                """.trimIndent()
            )
                .bind("clinicUserId", clinicUserId)
                .map { rs, _ ->
                    Client(
                        id = UUID.fromString(rs.getString("id")),
                        name = rs.getString("name"),
                        surname = rs.getString("surname"),
                        phone = rs.getString("phone")
                    )
                }
                .list()
        }
    }
}

