package com.example.recetario.ui.screens

import com.example.recetario.data.db.FavoriteRecipeDao
import com.example.recetario.data.db.RecipeDao
import com.example.recetario.data.db.UserDao
import com.example.recetario.data.model.User
import com.example.recetario.data.repository.RecetarioRepository
import com.example.recetario.data.repository.UserRepository
import com.example.recetario.data.repository.UserRepositoryImpl
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito.*

class LoginScreenTest {
    @Mock
    private lateinit var userDao: UserDao

    @Mock
    private lateinit var recipeDao: RecipeDao

    @Mock
    private lateinit var favoriteRecipeDao: FavoriteRecipeDao

    // The subjects under test
    private lateinit var recetarioRepository: RecetarioRepository
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        recetarioRepository = RecetarioRepository(recipeDao, userDao, favoriteRecipeDao)
        userRepository = UserRepositoryImpl.getInstance(recetarioRepository)
    }

    @After
    fun teardown() {
        // No cleanup needed for mocks
    }

    private val mockUser = User(
        id = "1",
        firstname = "John",
        lastname = "Doe",
        email = "john@example.com",
        username = "johndoe",
        password = "pass123",
        userType = "REGULAR"
    )

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