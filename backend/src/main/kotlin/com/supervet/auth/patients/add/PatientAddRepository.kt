package com.supervet.auth.patients.add

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.useTransactionUnchecked
import org.postgresql.util.PSQLException
import java.util.*

private const val UNIQUE_VIOLATION = "23505"

class AddPatientRepository(private val jdbi: Jdbi) {

    fun savePatient(patientAddRequest: PatientAddRequest) {

        try {
            jdbi.useTransactionUnchecked { handle ->
                val patientId = UUID.randomUUID()
                handle.createUpdate(
                    """
                    INSERT INTO patients (owner_id, owner_name, patient_id, name, breed, age, weight, status, appointment)
                    VALUES (:ownerId, :ownerName, :patientId, :name, :breed, :age, :weight, :status, :appointment)
                    """.trimIndent()
                )
                    .bind("owner_id", patientAddRequest.owner_id)
                    .bind("owner_name", patientAddRequest.owner_name)
                    .bind("patient_id", patientId)
                    .bind("name", patientAddRequest.name)
                    .bind("breed", patientAddRequest.breed)
                    .bind("age", patientAddRequest.age)
                    .bind("weight", patientAddRequest.weight)
                    .bind("status", patientAddRequest.status)
                    .bind("appointment", patientAddRequest.appointment)
                    .execute()
            }
        } catch (e: Exception) {
            val cause = e.cause
            if (cause is PSQLException && cause.sqlState == UNIQUE_VIOLATION) {
                throw IllegalArgumentException("El paciente ya existe con el ID proporcionado.", e)
            } else {
                throw e
            }
        }
    }
}

