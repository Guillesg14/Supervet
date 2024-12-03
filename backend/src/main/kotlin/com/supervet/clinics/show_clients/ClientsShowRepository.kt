package com.supervet.clinics.show_clients

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import java.util.*


class ShowClientsRepository(private val jdbi: Jdbi) {
    fun getClientsByClinicId(clinicId: String): List<Client> {
        return jdbi.inTransactionUnchecked { handle ->
            handle.createQuery(
                """
                SELECT id, name, surname, phone
                FROM clients
                WHERE clinic_id = :clinic_id
                """.trimIndent()
            )
                .bind("clinic_id", UUID.fromString(clinicId))
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

