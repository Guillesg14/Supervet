package com.supervet.clinics.get_clients

import java.util.UUID

class GetClients(private val getClientsRepository: GetClientsRepository) {
    operator fun invoke(clinicUserId: UUID): List<Client> {
        return getClientsRepository.getClientsByClinicId(clinicUserId)
    }
}

data class Client(
    val id: UUID,
    val name: String,
    val surname: String,
    val phone: String
)

