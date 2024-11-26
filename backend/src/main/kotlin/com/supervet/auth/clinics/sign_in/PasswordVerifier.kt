package com.supervet.auth.clinics.sign_in

import at.favre.lib.crypto.bcrypt.BCrypt

class PasswordVerifier {
    fun isValid(password: String, hashedPassword: String) =
        BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified
}