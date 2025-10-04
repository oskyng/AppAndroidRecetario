package com.example.recetario.data.repository

import com.example.recetario.data.model.User

interface UserRepository {
    fun getAllUsers(): List<User>
    fun addUser(user: User)
    fun updateUser(id: String, updatedUser: User)
    fun deleteUser(id: String)
    suspend fun authenticate(username: String, password: String): User?
    fun findUserByUsername(username: String): User?
    fun filterByUserType(userType: String): List<User>
    fun advancedFilter(predicate: (User) -> Boolean): List<User>
    fun clearAllUsers()
}