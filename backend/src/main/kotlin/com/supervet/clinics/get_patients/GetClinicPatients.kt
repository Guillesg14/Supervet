package com.supervet.clinics.get_patients

import java.util.UUID

class GetClinicPatients (private val getClinicPatientsRepository: GetClinicPatientsRepository) {
    operator fun invoke(clientId: UUID): List<Patient>{
        return getClinicPatientsRepository.getPatientsByClientId(clientId)
    }
}
data class Patient(
    val id: UUID,
    val name: String,
    val breed: String,
    val age: Int,
    val weight: Int,
    val clientId: UUID
)
