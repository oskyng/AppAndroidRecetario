package com.example.recetario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recetario.util.SettingsState

@Composable
fun SettingsScreen(
    settingsState: SettingsState,
    onSettingsStateChange: (SettingsState) -> Unit
) {
    var notificationsEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Configuraciones",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.semantics { contentDescription = "Título de la pantalla de configuraciones" }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Modo Oscuro", modifier = Modifier.weight(1f))
            Switch(
                checked = settingsState.isDark,
                onCheckedChange = { onSettingsStateChange(settingsState.copy(isDark = it)) },
                modifier = Modifier.semantics { contentDescription = "Interruptor para activar o desactivar el modo oscuro" }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Modo Alto Contraste", modifier = Modifier.weight(1f))
            Switch(
                checked = settingsState.isHighContrast,
                onCheckedChange = { onSettingsStateChange(settingsState.copy(isHighContrast = it)) },
                modifier = Modifier.semantics { contentDescription = "Interruptor para activar o desactivar el modo de alto contraste" }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Navegación Simplificada", modifier = Modifier.weight(1f))
            Switch(
                checked = settingsState.isSimplifiedNavigation,
                onCheckedChange = { onSettingsStateChange(settingsState.copy(isSimplifiedNavigation = it)) },
                modifier = Modifier.semantics { contentDescription = "Interruptor para activar o desactivar la navegación simplificada" }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Tamaño de Texto: ${settingsState.fontScale.times(100).toInt()}%")
        Slider(
            value = settingsState.fontScale,
            onValueChange = { onSettingsStateChange(settingsState.copy(fontScale = it)) },
            valueRange = 0.8f..1.5f,
            steps = 6,
            modifier = Modifier.semantics { contentDescription = "Control deslizante para ajustar el tamaño de texto entre 80% y 150%" }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Tamaño de Iconos: ${settingsState.iconScale.times(100).toInt()}%")
        Slider(
            value = settingsState.iconScale,
            onValueChange = { onSettingsStateChange(settingsState.copy(iconScale = it)) },
            valueRange = 0.8f..1.5f,
            steps = 6,
            modifier = Modifier.semantics { contentDescription = "Control deslizante para ajustar el tamaño de iconos entre 80% y 150%" }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Notificaciones", modifier = Modifier.weight(1f))
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it },
                modifier = Modifier.semantics { contentDescription = "Interruptor para activar o desactivar notificaciones" }
            )
        }
    }
}

@Preview(showBackground = true, name = "SettingsScreenPreview")
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        settingsState = SettingsState(),
        onSettingsStateChange = {}
    )
}