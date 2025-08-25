package com.example.recetario.ui.common

import androidx.compose.ui.semantics.contentDescription
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.recetario.util.SharedState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopBar(title: String, showBack: Boolean, onBack: (() -> Unit)? = null, onSettings: (() -> Unit)? = null, onMenu: (() -> Unit)? = null) {
    var menuExpanded by remember { mutableStateOf(false) }
    val fontSize = SharedState.currentFontSize.sp
    TopAppBar(
        title = {
            Text(title, fontSize = fontSize * 1.2f, fontWeight = FontWeight.SemiBold)
        },
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = { onBack?.invoke() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atras")
                }
            } else {
                IconButton(onClick = { onMenu?.invoke() }) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                }
            }
        },
        actions = {
            if (onSettings != null) {
                IconButton(
                    onClick = {
                        onSettings.invoke()
                        menuExpanded = true
                    },
                    modifier = Modifier.semantics { contentDescription = "Abrir ajustes de fuente" }
                ) {
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
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}