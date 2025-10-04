package com.example.recetario.data.auth

class LoginViewModel(private val repo: AuthRepository) {
    fun login(email: String, password: String): LoginResult {
        if (email.isBlank() || password.isBlank()) {
            return LoginResult(false, "Campos vacíos")
        }
        return repo.authenticate(email, password)
    }
}