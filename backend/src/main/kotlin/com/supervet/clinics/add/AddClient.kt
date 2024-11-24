package com.supervet.clinics.add

class AddClient(private val addClientRepository: AddClientRepository) {
    operator fun invoke(addClientRequest: AddClientRequest){
        addClientRepository.saveClient(addClientRequest)
    }

}