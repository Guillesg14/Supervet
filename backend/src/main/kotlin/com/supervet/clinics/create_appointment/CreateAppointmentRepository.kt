package com.supervet.clinics.create_appointment

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.useTransactionUnchecked
import java.util.*

class CreateAppointmentRepository(private val jdbi: Jdbi) {

    fun saveAppointment(createAppointmentRequest: CreateAppointmentRequest, patientId: UUID) {
        jdbi.useTransactionUnchecked { handle ->
            handle.createUpdate(
                """
                    INSERT INTO appointments (id, patient_id, appointment, created_at)
                    VALUES (:id, :patientId, :appointment, now())
                    """.trimIndent()
            )
                .bind("id", UUID.randomUUID())
                .bind("patientId", patientId)
                .bind("appointment", createAppointmentRequest.appointment)
                .execute()
        }
    }
}