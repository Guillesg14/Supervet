package com.supervet.clients.get_patients_appointment

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import java.util.*

class GetClientPatientsAppointmentsRepository(private val jdbi: Jdbi) {
    fun getClientPatientsAppointmentsByUserId(userId: UUID): List<AppointmentInfo> {
        return jdbi.inTransactionUnchecked { handle ->
            handle.createQuery(
                """
                    SELECT 
                        a.id AS appointment_id,
                        p.id AS patient_id,
                        a.appointment AS appointment_name,
                        p.name AS patient_name
                    FROM 
                        clients c
                    JOIN 
                        patients p ON p.client_id = c.id
                    JOIN 
                        appointments a ON a.patient_id = p.id
                    WHERE 
                        c.user_id = :userId
                    ORDER BY 
                        a.created_at DESC
                """.trimIndent()
            )
                .bind("userId", userId)
                .map { rs, _ ->
                    AppointmentInfo(
                        appointmentId = UUID.fromString(rs.getString("appointment_id")),
                        patientId = UUID.fromString(rs.getString("patient_id")),
                        appointment = rs.getString("appointment_name"),
                        patientName = rs.getString("patient_name")
                    )
                }
                .list()
                .orEmpty()
        }
    }
}
