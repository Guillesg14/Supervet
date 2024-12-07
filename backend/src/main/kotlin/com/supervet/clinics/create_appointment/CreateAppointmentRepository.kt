package com.supervet.clinics.create_appointment

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.useTransactionUnchecked
import java.util.*

class CreateAppointmentRepository(private val jdbi: Jdbi) {

    fun saveAppointment(createAppointmentRequest: CreateAppointmentRequest) {
        jdbi.useTransactionUnchecked { handle ->
            handle.createUpdate(
                """
                    INSERT INTO appointments (id, patient_id, appointment)
                    VALUES (:id, :patientId, :appointment)
                    """.trimIndent()
            )
                .bind("id", UUID.randomUUID())
                .bind("patientId", createAppointmentRequest.patientId)
                .bind("appointment", createAppointmentRequest.appointment)
                .execute()
        }
    }
}