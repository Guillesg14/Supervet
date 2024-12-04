package com.supervet.clinics.show_clients

import java.util.UUID

class ClientsShow(private val clientsShowRepository: ShowClientsRepository) {
    operator fun invoke(clientsShowRequest: ClientsShowRequest): List<Client> {
        return clientsShowRepository.getClientsByClinicId(clientsShowRequest.clinicId)
    }
}

data class Client(
    val id: UUID,
    val name: String,
    val surname: String,
    val phone: String
)

