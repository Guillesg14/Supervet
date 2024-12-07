package com.supervet.clinics.create_appointment


class CreateAppointment(private val createAppointmentRepository: CreateAppointmentRepository) {
    operator fun invoke(createAppointmentRequest: CreateAppointmentRequest){
        createAppointmentRepository.saveAppointment(createAppointmentRequest)
    }
}
