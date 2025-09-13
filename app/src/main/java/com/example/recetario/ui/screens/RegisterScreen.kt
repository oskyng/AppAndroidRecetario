package com.example.recetario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recetario.data.model.ChefUser
import com.example.recetario.data.model.RegularUser
import com.example.recetario.data.model.User
import com.example.recetario.data.repository.UserRepository
import com.example.recetario.data.repository.UserRepositoryImpl

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    userRepository: UserRepository
) {
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var receiveNewsletter by remember { mutableStateOf(false) }
    var userType by remember { mutableStateOf("Regular") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registro de Usuario",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.semantics { contentDescription = "Título de la pantalla de registro" }
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = firstname,
            onValueChange = { firstname = it },
            label = { Text("Nombre") },
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Campo de texto para ingresar el nombre en el registro" },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = lastname,
            onValueChange = { lastname = it },
            label = { Text("Apellido") },
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Campo de texto para ingresar el apellido en el registro" },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Campo de texto para ingresar el nombre de usuario en el registro" },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Campo de texto para ingresar el correo electrónico en el registro" },
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
                .semantics { contentDescription = "Campo de texto para ingresar la contraseña en el registro" },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Campo de texto para confirmar la contraseña en el registro" },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Tipo de usuario", style = MaterialTheme.typography.bodyLarge)
        Row {
            RadioButton(
                selected = userType == "Regular",
                onClick = { userType = "Regular" },
                modifier = Modifier.semantics { contentDescription = "Opción de usuario regular" }
            )
            Text("Usuario Regular", modifier = Modifier.padding(start = 8.dp))
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = userType == "Chef",
                onClick = { userType = "Chef" },
                modifier = Modifier.semantics { contentDescription = "Opción de chef" }
            )
            Text("Cocinero Profesional", modifier = Modifier.padding(start = 8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (userType == "Regular") {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = receiveNewsletter,
                    onCheckedChange = { receiveNewsletter = it },
                    modifier = Modifier.semantics { contentDescription = "Casilla para recibir boletín de noticias" }
                )
                Text("Recibir boletín de recetas", modifier = Modifier.padding(start = 8.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

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
                try {
                    if (firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
                        throw IllegalArgumentException("Todos los campos son obligatorios")
                    }
                    if (password != confirmPassword) {
                        throw IllegalArgumentException("Las contraseñas no coinciden")
                    }
                    val user: User = when (userType) {
                        "Regular" -> RegularUser(firstname = firstname, lastname = lastname, email = email, username = username, password = password, receiveNewsletter = receiveNewsletter)
                        "Chef" -> ChefUser(firstname = firstname, lastname = lastname, email = email, username = username, password = password)
                        else -> throw IllegalArgumentException("Tipo de usuario inválido")
                    }
                    userRepository.addUser(user)
                    onRegisterSuccess()
                } catch (e: IllegalArgumentException) {
                    errorMessage = e.message ?: "Error al registrar"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .semantics { contentDescription = "Botón para registrarse" }
        ) {
            Text("Registrarse")
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { onBackToLogin() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD32F2F),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .semantics { role = Role.Button; contentDescription = "Volver a Iniciar Sesión" }
        ) {
            Text("Volver a Iniciar Sesión", modifier = Modifier.semantics { contentDescription = "Volver a Iniciar Sesión" })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreviewRegister() {
    MaterialTheme { RegisterScreen({}, {}, UserRepositoryImpl.INSTANCE) }
}