package com.supervet.clients.get_patients

import java.util.*


class GetClientPatients(private val getClientPatientsRepository: GetClientPatientsRepository){
    operator fun invoke(clientUserId: UUID): List<Patient> {
        return getClientPatientsRepository.getPatients(clientUserId)
    }
}
data class Patient(
    val id: UUID,
    val name: String,
    val breed: String,
    val age: Int,
    val weight: Int,
    val clientId: UUID,
)