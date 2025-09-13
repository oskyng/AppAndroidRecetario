package com.example.recetario.data.repository

import com.example.recetario.data.model.ChefUser
import com.example.recetario.data.model.RegularUser
import com.example.recetario.data.model.User
import java.util.UUID

class UserRepositoryImpl : UserRepository {
    companion object {
        val INSTANCE = UserRepositoryImpl()
    }

    private val users = mutableListOf<User>().apply {
        add(
            ChefUser(
                id = UUID.randomUUID().toString(),
                firstname = "Chef",
                lastname = "Test",
                email = "chef@test.com",
                username = "admin",
                password = "123qwe"
            )
        )
    }

    override fun getAllUsers(): List<User> = users

    override fun clearAllUsers() {
        users.clear()
        users.add(
            ChefUser(
                id = UUID.randomUUID().toString(),
                firstname = "Chef",
                lastname = "Test",
                email = "chef@test.com",
                username = "chef_test",
                password = "Test1234"
            )
        )
    }

    override fun addUser(user: User) {
        try {
            if (users.any { it.username == user.username }) {
                throw IllegalArgumentException("El usuario ya existe")
            }
            users.addIfNotExists(user)
        } catch (e: IllegalArgumentException) {
            println("Error al añadir usuario: ${e.message}")
            throw e
        }
    }

    override fun updateUser(id: String, updatedUser: User) {
        try {
            val index = users.indexOfFirst { it.id == id }
            if (index == -1) throw IllegalArgumentException("Usuario no encontrado")
            users[index] = updatedUser
        } catch (e: IllegalArgumentException) {
            println("Error al actualizar usuario: ${e.message}")
            throw e
        }
    }

    override fun deleteUser(id: String) {
        try {
            val user = users.find { it.id == id }
            if (user == null || !users.remove(user)) {
                throw IllegalArgumentException("Usuario no encontrado")
            }
        } catch (e: IllegalArgumentException) {
            println("Error al eliminar usuario: ${e.message}")
            throw e
        }
    }

    override fun authenticate(username: String, password: String): User? {
        try {
            if (username.isEmpty() || password.isEmpty()) {
                throw IllegalArgumentException("Usuario o contraseña vacíos")
            }
            var foundUser: User? = null
            for (user in users) {
                if (user.username == username && user.password == password) {
                    foundUser = user
                    break
                }
            }
            return foundUser
        } catch (e: IllegalArgumentException) {
            println("Error al autenticar: ${e.message}")
            throw e
        }
    }

    override fun findUserByUsername(username: String): User? {
        var index = 0
        while (index < users.size) {
            if (users[index].username == username) return users[index]
            index++
        }
        return null
    }

    override fun filterByUserType(userType: String): List<User> {
        try {
            if (userType.isEmpty()) throw IllegalArgumentException("Tipo de usuario vacío")
        } catch (e: IllegalArgumentException) {
            println("Error: ${e.message}")
            return emptyList()
        }

        val filtered = users.filter {
            when (userType) {
                "Todas" -> true
                "Regular" -> it is RegularUser
                "Chef" -> it is ChefUser
                else -> false
            }
        }

        filtered.forEach outer@{ user ->
            if (user.username.isEmpty()) return@outer
            println(user.username)
        }

        return filtered
    }

    override fun advancedFilter(predicate: (User) -> Boolean): List<User> {
        return users.filter(predicate)
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
                "Regular" -> it is RegularUser
                "Chef" -> it is ChefUser
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