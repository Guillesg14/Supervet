package com.supervet.clinics.get_appointments

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import java.util.*

class GetClinicPatientAppointmentsRepository(private val jdbi: Jdbi) {
    fun getAppointmentsByPatientId(patientId: UUID): List<Appointment>{
        return jdbi.inTransactionUnchecked { handle ->
            handle.createQuery(
                """
                    SELECT id, appointment, created_at
                    FROM appointments
                    WHERE patient_id = :patientId
                """.trimIndent()
            )
                .bind("patientId", patientId)
                .map{ rs, _ ->
                    Appointment(
                        id = UUID.fromString(rs.getString("id")),
                        appointment = rs.getString("appointment"),
                        created_at = rs.getDate("created_at"),
                    )
                }
                .list()
                .orEmpty()
        }
    }
}