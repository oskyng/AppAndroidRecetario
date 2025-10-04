package com.example.recetario

import com.example.recetario.data.auth.AuthRepository
import com.example.recetario.data.auth.LoginResult
import com.example.recetario.data.auth.LoginViewModel
import com.example.recetario.data.model.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class LoginViewModelTest {
    private val repo = mock<AuthRepository>()
    private val viewModel = LoginViewModel(repo)
    private val testUser = User(
        id = "1",
        firstname = "John",
        lastname = "Doe",
        email = "john@example.com",
        username = "johndoe",
        password = "pass123",
        userType = "REGULAR"
    )
    private val chefUser = User(
        id = "2",
        firstname = "Chef",
        lastname = "Test",
        email = "chef@test.com",
        username = "chef_test",
        password = "Test1234",
        userType = "CHEF"
    )

    @Test
    fun `correo no existe`() {
        whenever(repo.authenticate("noexiste@mail.com", "123456"))
            .thenReturn(LoginResult(false, "Correo no existe"))

        val result = viewModel.login("noexiste@mail.com", "123456")

        assertFalse(result.ok)
        assertEquals("Correo no existe", result.message)
    }

    @Test
    fun `password incorrecta`() {
        whenever(repo.authenticate("miguel@mail.com", "wrong"))
            .thenReturn(LoginResult(false, "Contraseña incorrecta"))

        val result = viewModel.login("miguel@mail.com", "wrong")

        assertFalse(result.ok)
        assertEquals("Contraseña incorrecta", result.message)
    }

    @Test
    fun `login chef correcto`() {
        whenever(repo.authenticate("chef@test.com", "Test1234"))
            .thenReturn(LoginResult(true, user = chefUser))

        val result = viewModel.login("chef@test.com", "Test1234")

        assertTrue(result.ok)
        assertEquals("chef@test.com", result.user?.email)
    }

    @Test
    fun `login regular correcto`() {
        whenever(repo.authenticate("john@example.com", "pass123"))
            .thenReturn(LoginResult(true, user = testUser))

        val result = viewModel.login("john@example.com", "pass123")

        assertTrue(result.ok)
        assertEquals("john@example.com", result.user?.email)
    }

    @Test
    fun `campos vacíos`() {
        val result = viewModel.login("", "")

        assertFalse(result.ok)
        assertEquals("Campos vacíos", result.message)
        verify(repo, never()).authenticate(any(), any())
    }
}