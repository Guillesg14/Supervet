package com.supervet.clinics.delete_client

import java.util.*

class DeleteClient(
    private val deleteClientRepository: DeleteClientRepository
) {
    operator fun invoke(clientId: UUID, clinicUserId: UUID) {
        if (deleteClientRepository.clientBelongsToClinic(clientId, clinicUserId)) {
            deleteClientRepository.delete(clientId)
        }
    }
}