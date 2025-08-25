package com.example.recetario.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recetario.R
import com.example.recetario.util.SharedState
import androidx.compose.ui.text.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginApp(onGoRegister: () -> Unit, onGoForgot: () -> Unit, onGoHome: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val fontSize by remember { derivedStateOf { SharedState.currentFontSize.sp } }
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text("Recetario", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(imageVector = Icons.Filled.Settings, contentDescription = "Ajustes")
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        modifier = Modifier.semantics { contentDescription = "Menú de ajustes de fuente" }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Aumentar fuente", fontSize = fontSize) },
                            onClick = {
                                SharedState.increaseFontSize()
                                menuExpanded = false
                            },
                            modifier = Modifier.semantics { contentDescription = "Opción para aumentar tamaño de fuente" }
                        )
                        DropdownMenuItem(
                            text = { Text("Reducir fuente", fontSize = fontSize) },
                            onClick = {
                                SharedState.decreaseFontSize()
                                menuExpanded = false
                            },
                            modifier = Modifier.semantics { contentDescription = "Opción para reducir tamaño de fuente" }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp
                ),
                shape = MaterialTheme.shapes.large
            ) { Icon(Icons.Filled.Add, contentDescription = "Incrementar") }
        }
    ) {
        padding -> Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_receta),
                contentDescription = "Recetas",
                modifier = Modifier.fillMaxWidth(0.5f),
                contentScale = ContentScale.Fit
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
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Campo de usuario" },
                textStyle = TextStyle(fontSize = fontSize)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Campo de contraseña" },
                textStyle = TextStyle(fontSize = fontSize)
            )
            Button(
                onClick = {
                    val userExists = SharedState.users.any { it.username == username && it.password == password }
                    if (userExists) {
                        onGoHome()
                    } else {
                        errorMessage = "Credenciales incorrectas"
                    }
                },
                modifier = Modifier.fillMaxWidth().semantics {contentDescription = "Botón para iniciar sesión"}
            ) {
                Text("Iniciar sesión", fontSize = fontSize)
            }
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = fontSize,
                    modifier = Modifier.semantics { contentDescription = "Mensaje de error: $errorMessage" }
                )
            }
            TextButton(onClick = onGoRegister, modifier = Modifier.fillMaxWidth().semantics { contentDescription = "Enlace para registrarse" }) {
                Text("Crear cuenta", fontSize = fontSize)
            }
            TextButton(onClick = onGoForgot, modifier = Modifier.fillMaxWidth().semantics { contentDescription = "Enlace para recuperar contraseña" }) {
                Text("¿Olvidaste tu contraseña?", fontSize = fontSize)
            }
        }
    }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MaterialTheme { LoginApp(onGoRegister = { }, onGoForgot = { }, onGoHome = { })  }
}