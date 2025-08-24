package com.example.recetario.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recetario.data.Usuario
import com.example.recetario.ui.common.SimpleTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterApp(onBack: () -> Unit, onGoLogin: () -> Unit) {
    var firtsname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val countries = listOf("Chile", "Argentina", "Perú", "México")
    var countryExpanded by remember { mutableStateOf(false) }
    var countrySelected by remember { mutableStateOf(countries.first()) }
    var acceptTerms by remember { mutableStateOf(false) }

    val users = remember { mutableStateListOf<Usuario>() }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = { SimpleTopBar(title = "Registro", showBack = true, onBack = onBack) }
    ) {
            padding -> Box(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = firtsname,
                onValueChange = {firtsname = it},
                label = { Text("Nombres") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth().semantics { contentDescription = "Campo para nombres" },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            OutlinedTextField(
                value = lastname,
                onValueChange = {lastname = it},
                label = { Text("Apellidos") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth().semantics { contentDescription = "Campo para apellidos" },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            OutlinedTextField(
                value = email,
                onValueChange = {email = it},
                label = { Text("Correo") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth().semantics { contentDescription = "Campo para correo electrónico" },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            OutlinedTextField(
                value = username,
                onValueChange = {username = it},
                label = { Text("Usuario") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth().semantics { contentDescription = "Campo para nombre de usuario" },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            OutlinedTextField(
                value = password,
                onValueChange = {password = it},
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth().semantics { contentDescription = "Campo para contraseña" },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {confirmPassword = it},
                label = { Text("Confirmar contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth().semantics { contentDescription = "Campo para confirmar contraseña" },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            ExposedDropdownMenuBox(
                expanded = countryExpanded,
                onExpandedChange = { countryExpanded = !countryExpanded }
            ) {
                OutlinedTextField(
                    value = countrySelected,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("País") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryExpanded)
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth().semantics { contentDescription = "Campo para seleccionar pais" }
                )
                ExposedDropdownMenu(
                    expanded = countryExpanded,
                    onDismissRequest = { countryExpanded = false }
                ) {
                    countries.forEach { country ->
                        DropdownMenuItem(
                            text = { Text(country) },
                            onClick = {
                                countrySelected = country
                                countryExpanded = false
                            },
                            modifier = Modifier.semantics { contentDescription = "Pais $country" }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = acceptTerms,
                    onCheckedChange = { acceptTerms = it },
                    modifier = Modifier.semantics { contentDescription = "Casilla para aceptar términos y condiciones" }
                )
                Text(
                    text = "Acepto los términos y condiciones",
                    color = Color.Black,
                    modifier = Modifier.semantics { contentDescription = "Texto para aceptar términos y condiciones" }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (firtsname.isEmpty() || lastname.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                        errorMessage = "Todos los campos son obligatorios"
                    } else if (password != confirmPassword) {
                        errorMessage = "Las contraseñas no coinciden"
                    } else if (password.length < 6) {
                        errorMessage = "La contraseña debe tener al menos 6 caracteres"
                    } else if (!acceptTerms) {
                        errorMessage = "Debes aceptar los términos y condiciones"
                    } else {
                        users.add(Usuario(firtsname, lastname, email, username, password))
                        onGoLogin()
                    }
                },
                modifier = Modifier.fillMaxWidth().semantics { contentDescription = "Boton para registrarse" }
            ) {
                Text("Crear usuario")
            }
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.semantics { contentDescription = "Mensaje de error: $errorMessage" }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Volver a Iniciar Sesión",
                color = Color(0xFF6200EE),
                modifier = Modifier
                    .clickable { onGoLogin() }
                    .semantics { contentDescription = "Enlace para volver a iniciar sesión" }
            )
        }
    }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreviewRegister() {
    MaterialTheme { RegisterApp(onBack = {}, onGoLogin = {}) }
}