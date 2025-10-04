package com.example.recetario.screens

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.recetario.data.db.AppDatabase
import com.example.recetario.data.model.User
import com.example.recetario.data.repository.RecetarioRepository
import com.example.recetario.data.repository.UserRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class LoginScreenTest {
    private lateinit var database: AppDatabase
    private lateinit var repository: RecetarioRepository
    private lateinit var userRepository: UserRepositoryImpl

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        repository = RecetarioRepository(database.recipeDao(), database.userDao(), database.favoriteRecipeDao())
        userRepository = UserRepositoryImpl.getInstance(repository)
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testSuccessfulLogin() = runBlocking {
        val user = User(
            id = "1",
            firstname = "John",
            lastname = "Doe",
            email = "john@example.com",
            username = "johndoe",
            password = "pass123",
            userType = "REGULAR"
        )
        repository.insertUser(user)

        val authenticatedUser = userRepository.authenticate("johndoe", "pass123")
        assertNotNull(authenticatedUser)
        assertEquals("johndoe", authenticatedUser?.username)
        assertEquals("pass123", authenticatedUser?.password)
        assertEquals("REGULAR", authenticatedUser?.userType)
    }

    @Test
    fun testFailedLoginWrongPassword() = runBlocking {
        val user = User(
            id = "1",
            firstname = "John",
            lastname = "Doe",
            email = "john@example.com",
            username = "johndoe",
            password = "pass123",
            userType = "REGULAR"
        )
        repository.insertUser(user)

        val authenticatedUser = userRepository.authenticate("johndoe", "wrongpass")
        assertNull(authenticatedUser)
    }

    @Test
    fun testFailedLoginNonExistentUser() = runBlocking {
        val authenticatedUser = userRepository.authenticate("nonexistent", "pass123")
        assertNull(authenticatedUser)
    }

    @Test
    fun testFailedLoginEmptyCredentials() = runBlocking {
        try {
            userRepository.authenticate("", "")
            fail("Debería lanzar IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertEquals("Usuario o contraseña vacíos", e.message)
        }
    }
}