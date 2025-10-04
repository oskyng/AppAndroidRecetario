package com.example.recetario.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recetario.R
import com.example.recetario.data.db.AppDatabase
import com.example.recetario.data.model.User
import com.example.recetario.data.repository.RecetarioRepository
import com.example.recetario.data.repository.UserRepositoryImpl
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: (User) -> Unit,
    onRegisterClick: () -> Unit,
    onRecoverPasswordClick: () -> Unit,
    repository: RecetarioRepository
) {
    val coroutineScope = rememberCoroutineScope()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Text(
//            text = "Iniciar Sesión",
//            style = MaterialTheme.typography.headlineMedium,
//            modifier = Modifier.semantics { contentDescription = "Título de la pantalla de inicio de sesión" }
//        )
//        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Recetas",
            modifier = Modifier.fillMaxWidth(0.5f),
            contentScale = ContentScale.Fit
        )
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Campo de texto para ingresar el nombre de usuario" },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Campo de texto para ingresar la contraseña" },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.semantics { contentDescription = "Mensaje de error: $errorMessage" }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        val user = UserRepositoryImpl.getInstance(repository).authenticate(username, password)
                        if (user != null) {
                            onLoginSuccess(user)
                        } else {
                            errorMessage = "Usuario o contraseña incorrectos"
                        }
                    } catch (e: IllegalArgumentException) {
                        errorMessage = e.message ?: "Error al autenticar"
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .semantics { contentDescription = "Botón para iniciar sesión" }
        ) {
            Text("Iniciar Sesión")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRegisterClick() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1976D2),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .semantics { role = Role.Button; contentDescription = "Ir a registro" }
        ) {
            Text("Registrarse", modifier = Modifier.semantics { contentDescription = "Ir a registro" })
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { onRecoverPasswordClick() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD32F2F),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .semantics { role = Role.Button; contentDescription = "Recuperar contraseña" }
        ) {
            Text("Recuperar Contraseña", modifier = Modifier.semantics { contentDescription = "Recuperar contraseña" })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MaterialTheme { LoginScreen({}, {}, {}, RecetarioRepository(AppDatabase.getDatabase(androidx.compose.ui.platform.LocalContext.current).recipeDao(), AppDatabase.getDatabase(androidx.compose.ui.platform.LocalContext.current).userDao(), AppDatabase.getDatabase(androidx.compose.ui.platform.LocalContext.current).favoriteRecipeDao())) }
}