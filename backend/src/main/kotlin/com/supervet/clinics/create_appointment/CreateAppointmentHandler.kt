package com.supervet.clinics.create_appointment

import com.supervet.ktor.Handler
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*
class CreateAppointmentHandler (private val createAppointment: CreateAppointment) : Handler{
    override suspend fun invoke(ctx: RoutingContext){
        val patientId = UUID.fromString(ctx.call.parameters["patient-id"]!!)
        val addAppointmentRequest = ctx.call.receive<CreateAppointmentRequest>()

        try {
            createAppointment(addAppointmentRequest, patientId)
            ctx.call.respond(HttpStatusCode.Created)
        } catch (e:Exception){
                ctx.call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }

data class CreateAppointmentRequest(
    val appointment: String,
)