package com.supervet.clinics.create_patient

import java.util.*


class CreatePatient(private val createPatientRepository: CreatePatientRepository) {
    operator fun invoke(createPatientRequest: CreatePatientRequest, clinicId: UUID) {
        val clientId = UUID.fromString(createPatientRequest.clientId)

        if (!createPatientRepository.clientBelongsToClinic(clientId, clinicId)) {
            throw ClientDoesNotBelongToClinicException(clientId, clinicId)
        }

        createPatientRepository.savePatient(createPatientRequest)
    }
}

class ClientDoesNotBelongToClinicException(clientId: UUID, clinicId: UUID) : Exception("Client with id $clientId does not belong to clinic $clinicId.")
