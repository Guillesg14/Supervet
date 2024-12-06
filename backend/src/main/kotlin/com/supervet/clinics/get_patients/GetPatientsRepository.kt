package com.supervet.clinics.get_patients

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import java.util.*

class GetPatientsRepository(private val jdbi: Jdbi)  {
    fun getPatientsByClientId(clientId: UUID): List<Patient>{
        return jdbi.inTransactionUnchecked { handle ->
            handle.createQuery(
                """
                SELECT id, client_id, name, breed, age, weight, status
                FROM patients
                WHERE client_id = :clientId
                """.trimIndent()
            )
                .bind("clientId", clientId)
                .map { rs, _ ->
                    Patient(
                        id = UUID.fromString(rs.getString("id")),
                        name = rs.getString("name"),
                        breed = rs.getString("breed"),
                        age = rs.getInt("age"),
                        weight = rs.getInt("weight"),
                        status = rs.getString("status"),
                        clientId = UUID.fromString(rs.getString("client_id")),
                    )
                }
                .list()
        }
    }
}