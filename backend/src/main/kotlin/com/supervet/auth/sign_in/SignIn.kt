package com.supervet.auth.sign_in

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

class SignIn(private val signInRepository: SignInRepository) {
    operator fun invoke(clinicSignInRequest: ClinicSignInRequest): String{
        val existingUser = signInRepository.logClinic(clinicSignInRequest)
        val token = JWT.create()
            .withAudience("supervet")
            .withIssuer("supervet")
            .withClaim("type", "CLINIC")
            .withClaim("user_id", existingUser.id.toString())
            .withClaim("email", existingUser.email)
            .sign(Algorithm.HMAC512("supervet"))
        return token
    }
}