package com.example.recetario.data.repository

import com.example.recetario.data.model.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import java.util.UUID

class UserRepositoryImpl(private val repository: RecetarioRepository) : UserRepository {
    companion object {
        private var instance: UserRepositoryImpl? = null

        fun getInstance(repository: RecetarioRepository): UserRepositoryImpl {
            return instance ?: synchronized(this) {
                instance ?: UserRepositoryImpl(repository).also { instance = it }
            }
        }
    }

    init {
        runBlocking {
            val users = repository.getAllUsers().firstOrNull() ?: emptyList()
            if (users.isEmpty()) {
                repository.insertUser(
                    User(
                        id = UUID.randomUUID().toString(),
                        firstname = "Chef",
                        lastname = "Test",
                        email = "chef@test.com",
                        username = "chef_test",
                        password = "Test1234",
                        userType = "CHEF"
                    )
                )
            }
        }
    }

    override fun getAllUsers(): List<User> = runBlocking {
        repository.getAllUsers().first()
    }

    override fun addUser(user: User) {
        try {
            runBlocking {
                if (repository.getAllUsers().first().any { it.username == user.username }) {
                    throw IllegalArgumentException("El usuario ya existe")
                }
                repository.insertUser(user)
            }
        } catch (e: IllegalArgumentException) {
            println("Error al añadir usuario: ${e.message}")
            throw e
        }
    }

    override fun updateUser(id: String, updatedUser: User) {
        try {
            runBlocking {
                val existingUser = repository.getUserById(id)
                if (existingUser == null) throw IllegalArgumentException("Usuario no encontrado")
                repository.updateUser(updatedUser.copy(id = id))
            }
        } catch (e: IllegalArgumentException) {
            println("Error al actualizar usuario: ${e.message}")
            throw e
        }
    }

    override fun deleteUser(id: String) {
        try {
            runBlocking {
                val user = repository.getUserById(id)
                if (user == null) throw IllegalArgumentException("Usuario no encontrado")
                repository.deleteUser(user)
            }
        } catch (e: IllegalArgumentException) {
            println("Error al eliminar usuario: ${e.message}")
            throw e
        }
    }

    override suspend fun authenticate(username: String, password: String): User? {
        if (username.isEmpty() || password.isEmpty()) {
            throw IllegalArgumentException("Usuario o contraseña vacíos")
        }
        val users = repository.getAllUsers().firstOrNull() ?: emptyList()
        return users.find { it.username == username && it.password == password }
    }

    override fun findUserByUsername(username: String): User? = runBlocking {
        repository.getAllUsers().first().find { it.username == username }
    }

    override fun filterByUserType(userType: String): List<User> {
        try {
            if (userType.isEmpty()) throw IllegalArgumentException("Tipo de usuario vacío")
            return runBlocking {
                repository.getUsersByType(userType).first().filter {
                    when (userType) {
                        "Todas" -> true
                        "Regular" -> it.userType == "REGULAR"
                        "Chef" -> it.userType == "CHEF"
                        else -> false
                    }
                }
            }
        } catch (e: IllegalArgumentException) {
            println("Error: ${e.message}")
            return emptyList()
        }
    }

    override fun advancedFilter(predicate: (User) -> Boolean): List<User> = runBlocking {
        repository.getAllUsers().first().filter(predicate)
    }

    override fun clearAllUsers() {
        runBlocking {
            repository.deleteAllUsers()
            repository.insertUser(
                User(
                    id = java.util.UUID.randomUUID().toString(),
                    firstname = "Chef",
                    lastname = "Test",
                    email = "chef@test.com",
                    username = "chef_test",
                    password = "Test1234",
                    userType = "CHEF"
                )
            )
        }
    }

    fun logUsersByType(userType: String) {
        when (userType) {
            "Todas" -> println("Todas las categorías")
            "Regular" -> println("Usuario Regular seleccionado")
            "Chef" -> println("Cocinero Profesional seleccionado")
            else -> println("Categoría desconocida")
        }
        val filtered = advancedFilter {
            when (userType) {
                "Regular" -> it.userType == "REGULAR"
                "Chef" -> it.userType == "CHEF"
                else -> true
            }
        }
        println("Usuarios de tipo $userType: $filtered")
    }
}

// Propiedad de extensión
val List<User>.totalUsers: Int
    get() = size

// Función de extensión
fun MutableList<User>.addIfNotExists(user: User) {
    if (!contains(user)) add(user)
}