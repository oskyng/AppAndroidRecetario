package com.example.recetario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recetario.data.db.AppDatabase
import com.example.recetario.data.model.Recipe
import com.example.recetario.data.model.User
import com.example.recetario.data.repository.RecetarioRepository
import com.example.recetario.data.repository.RecipeRepository
import com.example.recetario.data.repository.RecipeRepositoryImpl
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(
    repository: RecetarioRepository,
    currentUser: User?,
    onNavigateToDetail: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var selectedCategory by remember { mutableStateOf("Todas") }
    var expanded by remember { mutableStateOf(false) }
    val categories = listOf("Todas", "Desayuno", "Almuerzo", "Ensalada", "Postre")
    val recipes by repository.getAllRecipes().collectAsState(initial = emptyList())
    val favoriteRecipes by currentUser?.let { repository.getFavoriteRecipes(it.id) }?.collectAsState(initial = emptyList()) ?: mutableStateOf(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Lista de Recetas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.semantics { contentDescription = "Título de la pantalla de lista de recetas" }
        )
        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                readOnly = true,
                value = selectedCategory,
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
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            selectedCategory = category
                            expanded = false
                        },
                        modifier = Modifier.semantics { contentDescription = "Opción de categoría: $category" }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(recipes.filter { if (selectedCategory == "Todas") true else it.category == selectedCategory }) { recipe ->
                RecipeItem(
                    recipe = recipe,
                    isFavorite = favoriteRecipes.contains(recipe),
                    onAddToFavorites = {
                        currentUser?.let {
                            coroutineScope.launch { repository.addFavorite(it.id, recipe.id) }
                        }
                    },
                    onRemoveFromFavorites = {
                        currentUser?.let {
                            coroutineScope.launch { repository.removeFavorite(it.id, recipe.id) }
                        }
                    },
                    onClick = { onNavigateToDetail(recipe.id) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun RecipeItem(
    recipe: Recipe,
    isFavorite: Boolean,
    onAddToFavorites: () -> Unit,
    onRemoveFromFavorites: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Receta: ${recipe.name}" },
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.semantics { contentDescription = "Nombre de la receta: ${recipe.name}" }
                )
                Text(
                    text = "Descripción: ${recipe.description}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.semantics { contentDescription = "Descripción de la receta: ${recipe.description}" }
                )
                Text(
                    text = "Categoría: ${recipe.category}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.semantics { contentDescription = "Categoría: ${recipe.category}" }
                )
                Text(
                    text = "Tiempo: ${recipe.getFormattedTime()}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.semantics { contentDescription = "Tiempo de preparación: ${recipe.getFormattedTime()}" }
                )
            }
            IconButton(
                onClick = { if (isFavorite) onRemoveFromFavorites() else onAddToFavorites() },
                modifier = Modifier.semantics {
                    contentDescription = if (isFavorite) "Quitar ${recipe.name} de favoritos" else "Agregar ${recipe.name} a favoritos"
                }
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeListScreenPreview() {
    MaterialTheme {
        RecipeListScreen(
            repository = RecetarioRepository(
                AppDatabase.getDatabase(LocalContext.current).recipeDao(),
                AppDatabase.getDatabase(LocalContext.current).userDao(),
                AppDatabase.getDatabase(LocalContext.current).favoriteRecipeDao()
            ),
            currentUser = null,
            onNavigateToDetail = {}
        )
    }
}