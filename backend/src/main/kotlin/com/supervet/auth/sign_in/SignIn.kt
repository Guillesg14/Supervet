package com.supervet.auth.sign_in

import java.util.*

class SignIn(
    private val signInRepository: SignInRepository,
    private val passwordVerifier: PasswordVerifier,
    private val jwtTokenCreator: JwtTokenCreator
) {
    operator fun invoke(clinicSignInRequest: ClinicSignInRequest): String {
        val user = signInRepository.getUserFrom(clinicSignInRequest.email)
            ?: throw UserDoesNotExistException(clinicSignInRequest.email)

        if (!passwordVerifier.isValid(clinicSignInRequest.password, user.password)) {
            throw WrongPasswordException(user.email)
        }

        return jwtTokenCreator.createToken(user)
    }
}

data class User(val id: UUID, val email: String, val password: String, val type: String)

class UserDoesNotExistException(email: String) : Exception("User with email $email does not exist.")
class WrongPasswordException(email: String) : Exception("Wrong password for user with email $email")