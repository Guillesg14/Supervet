package com.supervet.clients.get_clinic_info

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import java.util.*

class GetClinicInfoRepository(private val jdbi: Jdbi) {
    fun getClinicInfoByUserId(userId: UUID): ClinicInfo =
        jdbi.inTransactionUnchecked { handle ->
            try {
                handle.createQuery(
                 """
                     SELECT clinics.id, clinics.phone, clinics.address
                     FROM clinics
                     JOIN clients ON clinics.id = clients.clinic_id
                     WHERE clients.user_id = :user_id                      
                 """.trimIndent()
                )
                    .bind("user_id" , userId)
                    .map { rs, _ ->
                        ClinicInfo(
                            id = UUID.fromString(rs.getString("id")),
                            phone = rs.getString("phone"),
                            address = rs.getString("address")
                        )
                    }
                    .one()
            }catch (e: Exception) {
                when (e) {
                    is IllegalStateException -> throw ClientDoesNotExistException(userId)
                    else -> throw e
                }
            }
        }
}