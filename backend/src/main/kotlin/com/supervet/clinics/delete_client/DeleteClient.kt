package com.supervet.clinics.delete_client

import java.util.*

class DeleteClient(
    private val deleteClientRepository: DeleteClientRepository
) {
    operator fun invoke(clientId: UUID, clinicUserId: UUID) {
        if (!deleteClientRepository.clientBelongsToClinic(clientId, clinicUserId)) {
            throw ClientDoesNotBelongToClinicException(clientId, clinicUserId)
        }

        deleteClientRepository.delete(clientId)
    }
}

class ClientDoesNotBelongToClinicException(clientId: UUID, clinicId: UUID) : Exception("Client with id $clientId does not belong to clinic $clinicId.")
