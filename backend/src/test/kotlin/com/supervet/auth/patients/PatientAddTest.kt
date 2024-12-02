package com.supervet.auth.patients

import com.supervet.acceptance.helpers.testApplicationWithDependencies
import io.ktor.client.request.*
import io.ktor.http.*
import org.jdbi.v3.core.kotlin.mapTo
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import kotlin.test.assertNotNull

class PatientAddTest {
    @Test
    fun `should add a new patient`() = testApplicationWithDependencies { jdbi, client, customConfig ->
        val patientId = UUID.randomUUID()
        val patientAddPayload = mapOf(
            "owner_id" to "test-owner-id",
            "owner_name" to "Test Owner",
            "patient_id" to patientId.toString(),
            "name" to "Buddy",
            "breed" to "Golden Retriever",
            "age" to "3",
            "weight" to 25,
            "status" to "Healthy",
            "appointment" to "2024-12-02T10:00:00"
        )

        val response = client.post("auth/patients/add") {
            contentType(ContentType.Application.Json)
            setBody(patientAddPayload)
        }

        assertEquals(HttpStatusCode.Created, response.status)

        // Verificamos que el paciente se haya guardado en la base de datos
        val createdPatient = jdbi.withHandleUnchecked { handle ->
            handle.createQuery(
                """
                select owner_id, owner_name, patient_id, name, breed, age, weight, status, appointment
                from patients
                where patient_id = :patient_id
                """.trimIndent()
            )
                .bind("patient_id", patientId)
                .mapTo<Map<String, Any?>>()
                .one()
        }

        assertNotNull(createdPatient, "El paciente debería existir en la base de datos")
        assertEquals("test-owner-id", createdPatient["owner_id"])
        assertEquals("Test Owner", createdPatient["owner_name"])
        assertEquals("Buddy", createdPatient["name"])
        assertEquals("Golden Retriever", createdPatient["breed"])
        assertEquals("3", createdPatient["age"])
        assertEquals(25, createdPatient["weight"])
        assertEquals("Healthy", createdPatient["status"])
        assertEquals("2024-12-02T10:00:00", createdPatient["appointment"].toString())
    }
}