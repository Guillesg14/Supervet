
package com.supervet.auth.sign_in

import java.util.*

class SignIn(
    private val signInRepository: SignInRepository,
    private val passwordVerifier: PasswordVerifier,
    private val jwtTokenCreator: JwtTokenCreator
) {
    operator fun invoke(clinicSignInRequest: ClinicSignInRequest): String {
        // En el caso de uso cuando tenemos varias responsabilidad (buscar user, verificar contraseña, otros)
        // Lo usamos para gestionar que otros cumplan esas responsabilidades
        val user = signInRepository.getUserFrom(clinicSignInRequest.email)
            ?: throw UserDoesNotExistException(clinicSignInRequest.email)

        if (!passwordVerifier.isValid(clinicSignInRequest.password, user.password)) {
            throw WrongPasswordException(user.email)
        }

        return jwtTokenCreator.createToken(user)
    }
}

// Las clases y excepciones de tu dominio van en el caso de uso
data class User(val id: UUID, val email: String, val password: String)

// Los mensajes de error que generas van al stack trace, son para entenderlas tu, no el cliente
class UserDoesNotExistException(email: String) : Exception("User with email $email does not exist.")
class WrongPasswordException(email: String) : Exception("Wrong password for user with email $email")
