package com.supervet.auth.patients.add



class PatientAdd(private val patientAddRepository: AddPatientRepository) {
    operator fun invoke(patientAddRequest: PatientAddRequest) {
       patientAddRepository.savePatient(patientAddRequest)
    }
}

class UserAlreadyExistsException(email: String) : Exception("User with email $email already exists.")