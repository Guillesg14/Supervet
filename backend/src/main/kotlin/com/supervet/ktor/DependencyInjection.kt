package com.supervet.ktor

import com.supervet.auth.clinics.sign_up.ClinicSignUp
import com.supervet.auth.clinics.sign_up.SignUpHandler
import com.supervet.auth.clinics.sign_up.SignUpRepository
import com.supervet.auth.clients.sign_up.AddClientHandler
import com.supervet.auth.clients.sign_up.AddClientRepository
import com.supervet.auth.clients.sign_up.ClientSignUp
import com.supervet.clinics.create_patient.CreatePatientHandler
import com.supervet.clinics.create_patient.CreatePatientRepository
import com.supervet.clinics.create_patient.CreatePatient
import com.supervet.clinics.get_clients.GetClients
import com.supervet.clinics.get_clients.GetClientsHandler
import com.supervet.clinics.get_clients.GetClientsRepository
import com.supervet.auth.sign_in.*
import com.supervet.clinics.delete_client.DeleteClient
import com.supervet.clinics.delete_client.DeleteClientHandler
import com.supervet.clinics.delete_client.DeleteClientRepository
import com.supervet.clients.show_data.ClientDataShow
import com.supervet.clients.show_data.ClientDataShowHandler
import com.supervet.clients.show_data.ShowClientDataRepository
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
        bind<ShowClientDataRepository>() with singleton {ShowClientDataRepository(instance())}
        bind<CreatePatientRepository>() with singleton { CreatePatientRepository(instance()) }
        bind<SignInRepository>() with singleton { SignInRepository(instance()) }
        bind<SignUpRepository>() with singleton { SignUpRepository(instance()) }
        bind<AddClientRepository>() with singleton { AddClientRepository(instance()) }
        bind<GetClientsRepository>() with singleton { GetClientsRepository(instance()) }
        bind<ClientDataShow>() with singleton { ClientDataShow(instance()) }
        bind<CreatePatient>() with singleton { CreatePatient(instance()) }
        bind<GetClients>() with singleton { GetClients(instance()) }
        bind<ClientSignUp>() with singleton { ClientSignUp(instance()) }
        bind<PasswordVerifier>() with singleton { PasswordVerifier() }
        bind<JwtTokenCreator>() with singleton { JwtTokenCreator() }
        bind<SignIn>() with singleton { SignIn(instance(), instance(), instance()) }
        bind<ClinicSignUp>() with singleton { ClinicSignUp(instance()) }
        bind<ClientDataShowHandler>() with singleton {ClientDataShowHandler(instance())}
        bind<CreatePatientHandler>() with singleton { CreatePatientHandler(instance()) }
        bind<GetClientsHandler>() with singleton { GetClientsHandler(instance()) }
        bind<AddClientHandler>() with singleton { AddClientHandler(instance()) }
        bind<SignUpHandler>() with singleton { SignUpHandler(instance()) }
        bind<SignInHandler>() with singleton { SignInHandler(instance()) }
        bind<DeleteClientRepository>() with singleton { DeleteClientRepository(instance()) }
        bind<DeleteClient>() with singleton { DeleteClient(instance()) }
        bind<DeleteClientHandler>() with singleton { DeleteClientHandler(instance()) }
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