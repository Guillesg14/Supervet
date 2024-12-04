package com.supervet.clients.show_data

import java.util.UUID

class ClientDataShow(private val clientDataShowRepository: ShowClientDataRepository) {
    operator fun invoke(clientDataShowRequest: ClientDataShowRequest): List<Client> {
        val clients = clientDataShowRepository.getClientDataByUserId(clientDataShowRequest.userId)

        if (clients.isEmpty()) {
            throw ClientDoesNotExistException(clientDataShowRequest.userId)
        }

        return clients
    }
}
class ClientDoesNotExistException(userId: String) : Exception("no clients found for clinic with $userId")
data class Client(
    val user_id: UUID,
    val name: String,
    val surname: String,
    val phone: String
)

