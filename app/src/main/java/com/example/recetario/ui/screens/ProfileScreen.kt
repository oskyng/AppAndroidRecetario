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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recetario.data.model.ChefUser
import com.example.recetario.data.model.RegularUser
import com.example.recetario.data.model.User

@Composable
fun ProfileScreen(
    user: User?,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Perfil de Usuario",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.semantics { contentDescription = "Título de la pantalla de perfil" }
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (user != null) {
            Text(
                text = "Nombre: ${user.firstname} ${user.lastname}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.semantics { contentDescription = "Nombre completo: ${user.firstname} ${user.lastname}" }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Usuario: ${user.username}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.semantics { contentDescription = "Nombre de usuario: ${user.username}" }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Correo: ${user.email}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.semantics { contentDescription = "Correo electrónico: ${user.email}" }
            )
            Spacer(modifier = Modifier.height(8.dp))

            when (user) {
                is RegularUser -> {
                    Text(
                        text = "Tipo: Usuario Regular",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.semantics { contentDescription = "Tipo de usuario: Regular" }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    var receiveNewsletter by remember { mutableStateOf(user.receiveNewsletter) }
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
                }
                is ChefUser -> {
                    Text(
                        text = "Tipo: Cocinero Profesional",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.semantics { contentDescription = "Tipo de usuario: Chef" }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Recetas creadas: ${user.createdRecipes.size}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.semantics { contentDescription = "Número de recetas creadas: ${user.createdRecipes.size}" }
                    )
                    if (user.createdRecipes.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Recetas:",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.semantics { contentDescription = "Lista de recetas creadas" }
                        )
                        user.createdRecipes.forEach { recipe ->
                            Text(
                                text = "- ${recipe.name}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.semantics { contentDescription = "Receta creada: ${recipe.name}" }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onLogout,
                modifier = Modifier.semantics { contentDescription = "Botón para cerrar sesión" }
            ) {
                Text("Cerrar Sesión")
            }
        } else {
            Text(
                text = "No hay usuario autenticado",
                modifier = Modifier.semantics { contentDescription = "Mensaje de usuario no autenticado" }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreviewProfile() {
    MaterialTheme { ProfileScreen(null) {} }
}