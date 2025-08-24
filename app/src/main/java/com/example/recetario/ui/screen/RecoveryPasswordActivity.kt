package com.example.recetario.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recetario.ui.common.SimpleTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoveryPasswordApp(onBack: () -> Unit, onGoLogin: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    Scaffold (
        topBar = { SimpleTopBar(title = "Recuperar contreseña", showBack = true, onBack = onBack) }
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
                value = email,
                onValueChange = {email = it},
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth().semantics { contentDescription = "Campo para correo electrónico" }
            )
            Button(
                onClick = {
                    if (email.isNotEmpty()) {
                        successMessage = "Se ha enviado un enlace de recuperación a $email"
                    }
                },
                modifier = Modifier.fillMaxWidth().semantics { contentDescription = "Botón para enviar enlace de recuperación" }
            ) {
                Text("Recuperar contraseña")
            }
            if (successMessage.isNotEmpty()) {
                Text(
                    text = successMessage,
                    color = Color.Green,
                    modifier = Modifier.semantics { contentDescription = "Mensaje de éxito: $successMessage" }
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
fun GreetingPreview2() {
    MaterialTheme { RecoveryPasswordApp(onBack = {}, onGoLogin = {}) }
}