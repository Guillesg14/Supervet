package com.supervet.clinics.delete_client

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.useTransactionUnchecked
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import java.util.*

class DeleteClientRepository(
    private val jdbi: Jdbi
) {
    fun clientBelongsToClinic(clientId: UUID, clinicId: UUID): Boolean =
        jdbi.withHandleUnchecked { handle ->
            handle.createQuery(
                """
                    select 1
                    from clients
                    where user_id = :clientId and clinic_id = :clinicId
                """.trimIndent()
            )
                .bind("clientId", clientId)
                .bind("clinicId", clinicId)
                .mapTo(Boolean::class.java)
                .findOne().orElse(false)
        }

    fun delete(clientId: UUID) =
        jdbi.useTransactionUnchecked { handle ->
            handle.createUpdate(
                """
                    delete from clients
                    where user_id = :clientId
                """.trimIndent()
            )
                .bind("clientId", clientId)
                .execute()

            handle.createUpdate(
                """
                    DELETE FROM users
                    WHERE id = :clientId
                """.trimIndent()
            )
                .bind("clientId", clientId)
                .execute()
        }
}