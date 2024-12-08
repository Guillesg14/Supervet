package com.supervet.clinics.get_appointments

import java.util.Date
import java.util.UUID

class GetClinicPatientAppointments(private val getClinicPatientAppointmentsRepository: GetClinicPatientAppointmentsRepository){
    operator fun invoke(patientId: UUID): List<Appointment>{
        return getClinicPatientAppointmentsRepository.getAppointmentsByPatientId(patientId)
    }
}
data class Appointment(
    val id: UUID,
    val appointment: String,
    val created_at: Date,
)