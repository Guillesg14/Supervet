package com.supervet.clients.get_clinic_info

import java.util.UUID

class GetClinicInfo(private val getClinicInfoRepository: GetClinicInfoRepository) {
    operator fun invoke (userId: UUID): ClinicInfo {
        return getClinicInfoRepository.getClinicInfoByUserId(userId)
    }
}

class ClientDoesNotExistException(userId: UUID) : Exception("no client found with user id $userId")

data class ClinicInfo(
    val id: UUID,
    val phone: String,
    val address: String
)