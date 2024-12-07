package com.supervet.ktor

import com.supervet.auth.sign_in.SignInHandler
import com.supervet.auth.clinics.sign_up.SignUpHandler
import com.supervet.auth.clients.sign_up.AddClientHandler
import com.supervet.clinics.create_patient.CreatePatientHandler
import com.supervet.clients.get_info.GetInfoHandler
import com.supervet.clients.get_patients.GetClientPatientsHandler
import com.supervet.clinics.create_appointment.CreateAppointmentHandler
import com.supervet.clinics.get_clients.GetClientsHandler
import com.supervet.clinics.delete_client.DeleteClientHandler
import com.supervet.clinics.get_patients.GetClinicPatientsHandler
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

inline fun <reified T : Handler> executeInvoke(): suspend RoutingContext.() -> Unit = {
    val handler by this.call.application.closestDI().instance<T>()
    handler(this)
}

interface Handler {
    suspend operator fun invoke(ctx: RoutingContext)
}

fun Application.configureRouting() {
    install(ContentNegotiation) {
        jackson()
    }

    routing {
        route("auth") {
            post("sign-in", executeInvoke<SignInHandler>())

            route("clients") {
                post("sign-up", executeInvoke<AddClientHandler>())
            }

            route("clinics") {
                post("sign-up", executeInvoke<SignUpHandler>())
            }
        }

        authenticate("clinics") {
            route("clinics") {
                get("clients", executeInvoke<GetClientsHandler>())
                delete("delete-client/{client-id}", executeInvoke<DeleteClientHandler>())
                post("create-patient", executeInvoke<CreatePatientHandler>())
                get("clients/{client-id}/patients", executeInvoke<GetClinicPatientsHandler>())
                post("clients/{client-id}/patients/{patient-id}/appointments", executeInvoke<CreateAppointmentHandler>())
            }
        }

        authenticate("clients") {
            route("clients") {
                get("info", executeInvoke<GetInfoHandler>())
                get("patients", executeInvoke<GetClientPatientsHandler>())
            }
        }
    }
}