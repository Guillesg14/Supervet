package com.supervet.clinics.get_clients

import java.util.UUID

class GetClients(private val getClientsRepository: GetClientsRepository) {
    operator fun invoke(clinicId: UUID): List<Client> {
        return getClientsRepository.getClientsByClinicId(clinicId)
    }
}

data class Client(
    val id: UUID,
    val name: String,
    val surname: String,
    val phone: String
)

