package com.supervet.ktor

import com.example.auth.sign_in.SignInHandler
import com.example.auth.sign_up.SignUpHandler
import io.ktor.server.application.*
import org.kodein.di.bind
import org.kodein.di.ktor.di
import org.kodein.di.singleton

fun Application.configureDependencyInjection() {
    di {
        bind<SignUpHandler>() with singleton { SignUpHandler() }
        bind<SignInHandler>() with singleton { SignInHandler() }
    }
}