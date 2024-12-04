package com.supervet.ktor

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import sun.security.util.KeyUtil.validate

fun Application.configureSecurity() {
    authentication {
        jwt("clinics") {
            realm = "supervet"
            verifier(
                JWT
                    .require(Algorithm.HMAC512("supervet"))
                    .withAudience("supervet")
                    .withIssuer("supervet")
                    .build()
            )
            validate { token ->
                if (token.payload.getClaim("type").asString() == "CLINIC" && token.payload.getClaim("user_id").asString() != "") {
                    JWTPrincipal(token.payload)
                } else {
                    null
                }
            }
        }
        jwt("clients") {
            realm = "supervet"
            verifier(
                JWT
                    .require(Algorithm.HMAC512("supervet"))
                    .withAudience("supervet")
                    .withIssuer("supervet")
                    .build()
            )
            validate { token ->
                if (token.payload.getClaim("type").asString() == "CLIENT" && token.payload.getClaim("user_id").asString() != "") {
                    JWTPrincipal(token.payload)
                } else {
                    null
                }
            }
        }
    }
}
