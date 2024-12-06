package com.supervet.clinics.get_patients

import java.util.UUID

class GetPatients (private val getPatientsRepository: GetPatientsRepository) {
    operator fun invoke(clientId: UUID): List<Patient>{
        return getPatientsRepository.getPatientsByClientId(clientId)
    }
}
data class Patient(
    val id: UUID,
    val name: String,
    val breed: String,
    val age: Int,
    val weight: Int,
    val status: String,
)
