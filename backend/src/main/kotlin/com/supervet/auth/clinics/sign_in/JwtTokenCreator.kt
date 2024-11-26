package com.supervet.auth.clinics.sign_in

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

class JwtTokenCreator {
    fun createToken(user: User): String =
        JWT.create()
            .withAudience("supervet")
            .withIssuer("supervet")
            .withClaim("type", "CLINIC")
            .withClaim("user_id", user.id.toString())
            .withClaim("email", user.email)
            .sign(Algorithm.HMAC512("supervet"))
}