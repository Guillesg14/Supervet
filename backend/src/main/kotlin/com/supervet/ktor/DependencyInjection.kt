package com.supervet.ktor

import com.supervet.auth.sign_in.*
import com.supervet.auth.sign_up.SignUp
import com.supervet.auth.sign_up.SignUpHandler
import com.supervet.auth.sign_up.SignUpRepository
import com.supervet.clinics.addClient.AddClient
import com.supervet.clinics.addClient.AddClientHandler
import com.supervet.clinics.addClient.AddClientRepository
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.kodein.di.ktor.di
import org.kodein.di.singleton
import javax.sql.DataSource

fun Application.configureDependencyInjection() {
    di {
        bind<DataSource>() with singleton { createDataSource() }
        bind<Jdbi>() with singleton { createJdbi() }
        bind<SignInRepository>() with singleton { SignInRepository(instance()) }
        bind<SignUpRepository>() with singleton { SignUpRepository(instance()) }
        bind<AddClientRepository>() with singleton { AddClientRepository(instance()) }
        bind<AddClient>() with singleton { AddClient(instance())}
        bind<AddClientHandler>() with singleton { AddClientHandler(instance()) }
        bind<PasswordVerifier>() with singleton { PasswordVerifier() }
        bind<JwtTokenCreator>() with singleton { JwtTokenCreator() }
        bind<SignIn>() with singleton { SignIn(instance(), instance(), instance()) }
        bind<SignUp>() with singleton { SignUp(instance()) }
        bind<SignUpHandler>() with singleton { SignUpHandler(instance()) }
        bind<SignInHandler>() with singleton { SignInHandler(instance()) }

    }
}

private fun Application.createDataSource(): DataSource {
    val host = environment.config.property("database.host").getString()
    val port = environment.config.property("database.port").getString()
    val database = environment.config.property("database.database").getString()
    val user = environment.config.property("database.user").getString()
    val password = environment.config.property("database.password").getString()

    val hikariConfig = HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://${host}:${port}/${database}?user=${user}&password=${password}"
        driverClassName = "org.postgresql.Driver"
    }

    return HikariDataSource(hikariConfig)
}

private fun Application.createJdbi(): Jdbi {
    val dataSource by closestDI().instance<DataSource>()

    return Jdbi.create(dataSource)
        .installPlugin(PostgresPlugin())
        .installPlugin(KotlinPlugin(enableCoroutineSupport = true))
}