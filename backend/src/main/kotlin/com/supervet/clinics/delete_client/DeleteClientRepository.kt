package com.supervet.clinics.delete_client

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.useTransactionUnchecked
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import java.util.*

class DeleteClientRepository(
    private val jdbi: Jdbi
) {
    fun clientBelongsToClinic(clientId: UUID, clinicUserId: UUID): Boolean =
        jdbi.withHandleUnchecked { handle ->
            handle.createQuery(
                """
                    SELECT 1
                    FROM clients
                    WHERE id = :clientId AND clinic_id = (
                        SELECT c.id
                        FROM clients c
                        JOIN users u on u.id = c.user_id
                        WHERE u.id = :clinicUserId
                    )
                """.trimIndent()
            )
                .bind("clientId", clientId)
                .bind("clinicUserId", clinicUserId)
                .mapTo(Boolean::class.java)
                .findOne().orElse(false)
        }

    fun delete(clientId: UUID) =
        jdbi.useTransactionUnchecked { handle ->
            handle.createUpdate(
                """
                    delete from users
                    where id = (
                        select user_id
                        from clients 
                        where id = :clientId
                    )
                """.trimIndent()
            )
                .bind("clientId", clientId)
                .execute()
        }
}