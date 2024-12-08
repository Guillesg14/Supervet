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
import com.supervet.clients.get_clinic_info.GetClinicInfo
import com.supervet.clients.get_clinic_info.GetClinicInfoHandler
import com.supervet.clients.get_clinic_info.GetClinicInfoRepository
import com.supervet.clinics.delete_client.DeleteClient
import com.supervet.clinics.delete_client.DeleteClientHandler
import com.supervet.clinics.delete_client.DeleteClientRepository
import com.supervet.clients.get_info.GetInfo
import com.supervet.clients.get_info.GetInfoHandler
import com.supervet.clients.get_info.GetInfoRepository
import com.supervet.clients.get_patients.GetClientPatients
import com.supervet.clients.get_patients.GetClientPatientsHandler
import com.supervet.clients.get_patients.GetClientPatientsRepository
import com.supervet.clinics.create_appointment.CreateAppointment
import com.supervet.clinics.create_appointment.CreateAppointmentHandler
import com.supervet.clinics.create_appointment.CreateAppointmentRepository
import com.supervet.clinics.get_appointments.GetClinicPatientAppointments
import com.supervet.clinics.get_appointments.GetClinicPatientAppointmentsHandler
import com.supervet.clinics.get_appointments.GetClinicPatientAppointmentsRepository
import com.supervet.clinics.get_patients.GetClinicPatients
import com.supervet.clinics.get_patients.GetClinicPatientsHandler
import com.supervet.clinics.get_patients.GetClinicPatientsRepository
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
        bind<GetInfoRepository>() with singleton {GetInfoRepository(instance())}
        bind<CreatePatientRepository>() with singleton { CreatePatientRepository(instance()) }
        bind<SignInRepository>() with singleton { SignInRepository(instance()) }
        bind<SignUpRepository>() with singleton { SignUpRepository(instance()) }
        bind<AddClientRepository>() with singleton { AddClientRepository(instance()) }
        bind<GetClientsRepository>() with singleton { GetClientsRepository(instance()) }
        bind<GetInfo>() with singleton { GetInfo(instance()) }
        bind<CreatePatient>() with singleton { CreatePatient(instance()) }
        bind<GetClients>() with singleton { GetClients(instance()) }
        bind<ClientSignUp>() with singleton { ClientSignUp(instance()) }
        bind<PasswordVerifier>() with singleton { PasswordVerifier() }
        bind<JwtTokenCreator>() with singleton { JwtTokenCreator() }
        bind<SignIn>() with singleton { SignIn(instance(), instance(), instance()) }
        bind<ClinicSignUp>() with singleton { ClinicSignUp(instance()) }
        bind<GetInfoHandler>() with singleton {GetInfoHandler(instance())}
        bind<CreatePatientHandler>() with singleton { CreatePatientHandler(instance()) }
        bind<GetClientsHandler>() with singleton { GetClientsHandler(instance()) }
        bind<AddClientHandler>() with singleton { AddClientHandler(instance()) }
        bind<SignUpHandler>() with singleton { SignUpHandler(instance()) }
        bind<SignInHandler>() with singleton { SignInHandler(instance()) }
        bind<DeleteClientRepository>() with singleton { DeleteClientRepository(instance()) }
        bind<DeleteClient>() with singleton { DeleteClient(instance()) }
        bind<DeleteClientHandler>() with singleton { DeleteClientHandler(instance()) }
        bind<GetClinicPatientsRepository>() with singleton { GetClinicPatientsRepository(instance()) }
        bind<GetClinicPatients>() with singleton { GetClinicPatients(instance()) }
        bind<GetClinicPatientsHandler>() with singleton { GetClinicPatientsHandler(instance()) }
        bind<GetClientPatientsRepository>() with singleton { GetClientPatientsRepository(instance()) }
        bind<GetClientPatients>() with singleton { GetClientPatients(instance()) }
        bind<GetClientPatientsHandler>() with singleton { GetClientPatientsHandler(instance()) }
        bind<CreateAppointmentHandler>() with singleton { CreateAppointmentHandler(instance()) }
        bind<CreateAppointment>() with  singleton { CreateAppointment(instance()) }
        bind<CreateAppointmentRepository>() with singleton { CreateAppointmentRepository(instance()) }
        bind<GetClinicInfoHandler>() with singleton { GetClinicInfoHandler(instance()) }
        bind<GetClinicInfo>() with singleton { GetClinicInfo(instance()) }
        bind<GetClinicInfoRepository>() with singleton { GetClinicInfoRepository(instance()) }
        bind<GetClinicPatientAppointmentsHandler>() with singleton { GetClinicPatientAppointmentsHandler(instance()) }
        bind<GetClinicPatientAppointments>() with singleton { GetClinicPatientAppointments(instance()) }
        bind<GetClinicPatientAppointmentsRepository>() with singleton { GetClinicPatientAppointmentsRepository(instance()) }
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