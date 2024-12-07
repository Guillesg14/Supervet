package com.supervet.clinics.create_appointment

import java.util.*


class CreateAppointment(private val createAppointmentRepository: CreateAppointmentRepository) {
    operator fun invoke(createAppointmentRequest: CreateAppointmentRequest, patientId: UUID){
        createAppointmentRepository.saveAppointment(createAppointmentRequest, patientId)
    }
}
