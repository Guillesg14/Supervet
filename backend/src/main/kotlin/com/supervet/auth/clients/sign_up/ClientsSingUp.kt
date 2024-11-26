package com.supervet.auth.clients.sign_up

class ClientSignUp(private val clientSignUpRepository: AddClientRepository) {
    operator fun invoke(clientSignUpRequest: ClientSignUpRequest) {
        clientSignUpRepository.saveClient(clientSignUpRequest)
    }
}

class ClientAlreadyExistsException(email: String) : Exception("Client with email $email already exists.")
class ClinicDoesNotExistException(clinicId: String) : Exception("Clinic with id $clinicId does not exist.")