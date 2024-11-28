package com.supervet.auth.clinics.sign_up

class ClinicSignUp(private val clinicSignUpRepository: SignUpRepository) {
    operator fun invoke(clinicSignUpRequest: ClinicSignUpRequest) {
        clinicSignUpRepository.saveClinic(clinicSignUpRequest)
    }
}

class UserAlreadyExistsException(email: String) : Exception("User with email $email already exists.")