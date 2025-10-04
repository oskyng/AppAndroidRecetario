package com.example.recetario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recetario.data.db.AppDatabase
import com.example.recetario.data.repository.RecetarioRepository
import com.example.recetario.data.repository.UserRepositoryImpl
import kotlinx.coroutines.launch

@Composable
fun RecoverPasswordScreen(
    repository: RecetarioRepository,
    onBackToLogin: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var userFound by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Recuperar Contraseña",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            isError = errorMessage != null,
            supportingText = {
                if (errorMessage != null) {
                    Text(errorMessage!!)
                }
            },
            enabled = !userFound,
        )

        if (userFound) {
            TextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Nueva Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                isError = errorMessage != null,
                supportingText = {
                    if (errorMessage != null && userFound) {
                        Text(errorMessage!!)
                    }
                },
                singleLine = true
            )

            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Nueva Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                isError = errorMessage != null,
                supportingText = {
                    if (errorMessage != null && userFound) {
                        Text(errorMessage!!)
                    }
                },
                singleLine = true
            )
        }

        if (successMessage != null) {
            Text(
                text = successMessage!!,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Button(
            onClick = {
                coroutineScope.launch {
                    if (!userFound) {
                        val user = UserRepositoryImpl.getInstance(repository).advancedFilter { it.email == email }.firstOrNull()
                        if (user != null) {
                            userFound = true
                            errorMessage = null
                        } else {
                            errorMessage = "Correo no encontrado"
                        }
                    } else {
                        if (newPassword.isNotBlank() && newPassword == confirmPassword) {
                            try {
                                val user = UserRepositoryImpl.getInstance(repository).advancedFilter { it.email == email }.firstOrNull()
                                if (user != null) {
                                    repository.updateUser(user.copy(password = newPassword))
                                    successMessage = "Contraseña actualizada exitosamente"
                                    errorMessage = null
                                    email = ""
                                    newPassword = ""
                                    userFound = false
                                    kotlinx.coroutines.delay(1000)
                                    onBackToLogin()
                                } else {
                                    errorMessage = "Usuario no encontrado"
                                }
                            } catch (e: Exception) {
                                errorMessage = "Error al actualizar la contraseña: ${e.message}"
                            }
                        } else {
                            errorMessage = if (newPassword.isBlank()) "La nueva contraseña no puede estar vacía" else "Las contraseñas no coinciden"
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            content = { Text(if (userFound) "Actualizar Contraseña" else "Verificar Correo") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onBackToLogin() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD32F2F),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .semantics { contentDescription = "Volver a Iniciar Sesión" }
        ) {
            Text("Volver a Iniciar Sesión")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreviewRecoverPassword() {
    MaterialTheme { RecoverPasswordScreen(
        RecetarioRepository(
            AppDatabase.getDatabase(LocalContext.current).recipeDao(),
            AppDatabase.getDatabase(LocalContext.current).userDao(),
            AppDatabase.getDatabase(LocalContext.current).favoriteRecipeDao()
        )
    ) {} }
}