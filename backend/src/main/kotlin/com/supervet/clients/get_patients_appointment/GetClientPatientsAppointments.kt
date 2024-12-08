package com.supervet.clients.get_patients_appointment

import java.util.UUID

class GetClientPatientsAppointments( private val getClientPatientsAppointmentsRepository: GetClientPatientsAppointmentsRepository) {

    operator fun invoke(userId: UUID): List<AppointmentInfo> {
        return getClientPatientsAppointmentsRepository.getClientPatientsAppointmentsByUserId(userId)
    }
}


data class AppointmentInfo(
    val appointmentId: UUID,
    val patientId: UUID,
    val appointment: String
)