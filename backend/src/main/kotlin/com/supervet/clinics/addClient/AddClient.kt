package com.supervet.clinics.addClient

class AddClient(private val addClientRepository: AddClientRepository) {
    operator fun invoke(addClientRequest: AddClientRequest){
        addClientRepository.saveClient(addClientRequest)
    }

}