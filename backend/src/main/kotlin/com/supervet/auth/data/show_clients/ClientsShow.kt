package com.supervet.auth.data.show_clients

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
