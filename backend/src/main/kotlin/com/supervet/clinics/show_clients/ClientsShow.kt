package com.supervet.clinics.show_clients

import java.util.UUID

class ClientsShow(private val clientsShowRepository: ShowClientsRepository) {
    operator fun invoke(clientsShowRequest: ClientsShowRequest): List<Client> {
        val clients = clientsShowRepository.getClientsByClinicId(clientsShowRequest.clinicId)

        if (clients.isEmpty()) {
            throw ClientsDoesNotExistException(clientsShowRequest.clinicId)
        }

        return clients
    }
}
class ClientsDoesNotExistException(clinicId: String) : Exception("no clients found for clinic with $clinicId")
data class Client(
    val id: UUID,
    val name: String,
    val surname: String,
    val phone: String
)

