package com.supervet.clinics.get_clients

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import java.util.*

class GetClientsRepository(private val jdbi: Jdbi) {
    fun getClientsByClinicId(clinicId: UUID): List<Client> {
        return jdbi.inTransactionUnchecked { handle ->
            handle.createQuery(
                """
                SELECT id, name, surname, phone
                FROM clients
                WHERE clinic_id = :clinic_id
                """.trimIndent()
            )
                .bind("clinic_id", clinicId)
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

