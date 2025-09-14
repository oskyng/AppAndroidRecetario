package com.example.recetario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recetario.data.model.ChefUser
import com.example.recetario.data.model.Recipe
import com.example.recetario.data.model.User
import com.example.recetario.data.repository.RecipeRepository
import com.example.recetario.data.repository.RecipeRepositoryImpl
import com.example.recetario.util.SettingsState
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRecipeScreen(
    user: User?,
    recipeRepository: RecipeRepository,
    onRecipeCreated: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (user !is ChefUser) {
            Text(
                text = "Solo los cocineros profesionales pueden crear recetas",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.semantics { contentDescription = "Mensaje de acceso denegado para crear recetas" }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRecipeCreated,
                modifier = Modifier.semantics { contentDescription = "Botón para volver" }
            ) {
                Text("Volver")
            }
        } else {
            var name by remember { mutableStateOf("") }
            var description by remember { mutableStateOf("") }
            var ingredients by remember { mutableStateOf("") }
            var instructions by remember { mutableStateOf("") }
            var category by remember { mutableStateOf("Desayuno") }
            var timeMinutes by remember { mutableStateOf("") }
            var expanded by remember { mutableStateOf(false) }
            var errorMessage by remember { mutableStateOf("") }
            val categories = listOf("Desayuno", "Almuerzo", "Ensalada", "Postre")

            Text(
                text = "Crear Nueva Receta",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.semantics { contentDescription = "Título de la pantalla de creación de recetas" }
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre de la receta") },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Campo de texto para el nombre de la receta" },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Campo de texto para la descripción" },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = ingredients,
                onValueChange = { ingredients = it },
                label = { Text("Ingredientes (separados por comas)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Campo de texto para los ingredientes" },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = instructions,
                onValueChange = { instructions = it },
                label = { Text("Instrucciones (separadas por comas)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Campo de texto para las instrucciones" },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    readOnly = true,
                    value = category,
                    onValueChange = { },
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .semantics { contentDescription = "Selector de categoría" }
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                category = cat
                                expanded = false
                            },
                            modifier = Modifier.semantics { contentDescription = "Opción de categoría: $cat" }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = timeMinutes,
                onValueChange = { timeMinutes = it },
                label = { Text("Tiempo de preparación (minutos)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = "Campo de texto para el tiempo de preparación"
                    },
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
                    try {
                        if (name.isEmpty() || description.isEmpty() || ingredients.isEmpty() || instructions.isEmpty() || timeMinutes.isEmpty()) {
                            throw IllegalArgumentException("Todos los campos son obligatorios")
                        }
                        val recipeTime = timeMinutes.toIntOrNull() ?: throw IllegalArgumentException("Tiempo debe ser un número")
                        val recipe = Recipe(
                            name = name,
                            description = description,
                            ingredients = ingredients.split(",").map { it.trim() },
                            instructions = instructions.split(",").map { it.trim() },
                            category = category,
                            timeMinutes = recipeTime
                        )
                        recipeRepository.addRecipe(recipe)
                        user.addCreatedRecipe(recipe)
                        onRecipeCreated()
                    } catch (e: IllegalArgumentException) {
                        errorMessage = e.message ?: "Error al crear la receta"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Botón para crear receta" }
            ) {
                Text("Crear Receta")
            }
        }
    }
}

@Preview(showBackground = true, name = "SettingsScreenPreview")
@Composable
fun CreateRecipeScreenPreview() {
    val user = ChefUser(
        id = UUID.randomUUID().toString(),
        firstname = "Chef",
        lastname = "Test",
        email = "chef@test.com",
        username = "admin",
        password = "123qwe"
    )
    MaterialTheme { CreateRecipeScreen(null, RecipeRepositoryImpl.INSTANCE, {}) }
}