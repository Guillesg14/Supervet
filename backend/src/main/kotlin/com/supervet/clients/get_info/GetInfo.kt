package com.supervet.clients.get_info

import java.util.UUID

class GetInfo(private val clientDataShowRepository: GetInfoRepository) {
    operator fun invoke(clientId: UUID): Client {
        return clientDataShowRepository.getClientDataByUserId(clientId)
    }
}

class ClientDoesNotExistException(userId: UUID) : Exception("No client found with user id $userId")

data class Client(
    val id: UUID,
    val name: String,
    val surname: String,
    val phone: String
)


