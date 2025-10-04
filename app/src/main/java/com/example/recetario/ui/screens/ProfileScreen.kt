package com.example.recetario.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recetario.data.db.AppDatabase
import com.example.recetario.data.model.User
import com.example.recetario.data.repository.RecetarioRepository
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@Composable
fun ProfileScreen(
    user: User?,
    repository: RecetarioRepository,
    onLogout: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val createdRecipes by user?.let { u ->
        repository.getAllRecipes().collectAsState(initial = emptyList()).value.filter { r -> u.createdRecipes.contains(r.id) }
    }?.let { mutableStateOf(it) } ?: mutableStateOf(emptyList())
    var receiveNewsletter by remember { mutableStateOf(user?.receiveNewsletter ?: false) }

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

            Text(
                text = "Tipo: ${if (user.userType == "CHEF") "Cocinero Profesional" else "Usuario Regular"}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.semantics { contentDescription = "Tipo de usuario: ${user.userType}" }
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (user.userType == "REGULAR") {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = receiveNewsletter,
                        onCheckedChange = {
                            receiveNewsletter = it
                            coroutineScope.launch {
                                repository.updateUser(user.copy(receiveNewsletter = it))
                            }
                        },
                        modifier = Modifier.semantics { contentDescription = "Casilla para recibir boletín de noticias" }
                    )
                    Text("Recibir boletín de recetas", modifier = Modifier.padding(start = 8.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (user.userType == "CHEF") {
                Text(
                    text = "Recetas creadas: ${createdRecipes.size}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.semantics { contentDescription = "Número de recetas creadas: ${createdRecipes.size}" }
                )
                if (createdRecipes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Recetas:",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.semantics { contentDescription = "Lista de recetas creadas" }
                    )
                    createdRecipes.forEach { recipe ->
                        Text(
                            text = "- ${recipe.name}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.semantics { contentDescription = "Receta creada: ${recipe.name}" }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        user.id.let { repository.clearFavorites(it) }
                        onLogout()
                    }
                },
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
    MaterialTheme {
        ProfileScreen(
            user = User(firstname = "Chef", lastname = "Test", email = "chef@test.com", username = "chef_test", password = "Test1234", userType = "CHEF"),
            repository = RecetarioRepository(
                AppDatabase.getDatabase(LocalContext.current).recipeDao(),
                AppDatabase.getDatabase(LocalContext.current).userDao(),
                AppDatabase.getDatabase(LocalContext.current).favoriteRecipeDao()
            ),
            onLogout = {}
        )
    }
}