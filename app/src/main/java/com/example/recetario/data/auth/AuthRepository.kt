package com.example.recetario.data.auth

import android.os.Message
import com.example.recetario.data.model.User

data class LoginResult(
    val ok: Boolean,
    val message: String? = null,
    val user: User? = null
)

interface AuthRepository {
    fun authenticate(email: String, password: String): LoginResult
}