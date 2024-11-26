package com.supervet.auth.clients.sign_up

class ClientSignUp(private val clientSignUpRepository: AddClientRepository) {
    operator fun invoke(addClientRequest: AddClientRequest) {
        clientSignUpRepository.saveClient(addClientRequest)
    }
}