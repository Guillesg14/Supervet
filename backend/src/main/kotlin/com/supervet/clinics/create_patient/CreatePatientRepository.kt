package com.supervet.clinics.create_patient

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.useTransactionUnchecked
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import java.util.*

class CreatePatientRepository(private val jdbi: Jdbi) {
    fun clientBelongsToClinic(clientId: UUID, clinicId: UUID): Boolean =
        jdbi.withHandleUnchecked { handle ->
            handle.createQuery(
                """
                    select 1
                    from clients
                    where id = :clientId and clinic_id = (
                        select c.id
                        from users u
                        join clinics c on u.id = c.user_id
                        where u.id = :clinicUserId
                    )
                """.trimIndent()
            )
                .bind("clientId", clientId)
                .bind("clinicUserId", clinicId)
                .mapTo(Boolean::class.java)
                .findOne().orElse(false)
        }

    fun savePatient(createPatientRequest: CreatePatientRequest) {
        jdbi.useTransactionUnchecked { handle ->
            handle.createUpdate(
                """
                    INSERT INTO patients (id, client_id, name, breed, age, weight)
                    VALUES (:id, :clientId, :name, :breed, :age, :weight)
                    """.trimIndent()
            )
                .bind("id", UUID.randomUUID())
                .bind("clientId", UUID.fromString(createPatientRequest.clientId))
                .bind("name", createPatientRequest.name)
                .bind("breed", createPatientRequest.breed)
                .bind("age", createPatientRequest.age)
                .bind("weight", createPatientRequest.weight)
                .execute()
        }
    }
}

