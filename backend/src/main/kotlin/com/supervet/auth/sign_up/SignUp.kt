package com.supervet.auth.sign_up


class SignUp(private val signUpRepository: SignUpRepository) {
  operator fun invoke(clinicSignUpRequest: ClinicSignUpRequest) {
        signUpRepository.saveClinic(clinicSignUpRequest)
    }
}