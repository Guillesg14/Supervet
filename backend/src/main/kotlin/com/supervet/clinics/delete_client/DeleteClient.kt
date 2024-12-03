package com.supervet.clinics.delete_client

import java.util.*

class DeleteClient(
    private val deleteClientRepository: DeleteClientRepository
) {
    fun deleteClient(clientId: UUID, clinicId: UUID) {
        if (deleteClientRepository.clientBelongsToClinic(clientId, clinicId)) {
            deleteClientRepository.delete(clientId)
        }
    }
}