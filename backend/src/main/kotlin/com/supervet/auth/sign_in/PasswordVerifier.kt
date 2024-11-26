package com.supervet.auth.sign_in

import at.favre.lib.crypto.bcrypt.BCrypt

class PasswordVerifier {
    fun isValid(password: String, hashedPassword: String) =
        BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified
}