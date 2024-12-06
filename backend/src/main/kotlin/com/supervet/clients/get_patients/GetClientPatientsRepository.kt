package com.supervet.clients.get_patients

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import java.util.UUID

class GetClientPatientsRepository(private val jdbi: Jdbi){
    fun getPatients(clientUserId: UUID) = jdbi.withHandleUnchecked { handle ->
        handle.createQuery(
            """
                SELECT id, name, breed, age, weight, client_id
                FROM patients
                WHERE client_id = (
                    SELECT c.id
                    FROM users u
                    JOIN clients c ON u.id = c.user_id
                    WHERE u.id = :clientUserId
                )
            """.trimIndent()
        )
            .bind("clientUserId", clientUserId)
            .map { rs, _ ->
                Patient(
                    id = UUID.fromString(rs.getString("id")),
                    name = rs.getString("name"),
                    breed = rs.getString("breed"),
                    age = rs.getInt("age"),
                    weight = rs.getInt("weight"),
                    clientId = UUID.fromString(rs.getString("client_id")),
                )
            }
            .list()
    }


}