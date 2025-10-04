package com.example.recetario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.recetario.data.model.Recipe
import com.example.recetario.data.repository.RecetarioRepository

@Composable
fun RecipeDetailScreen(
    recipeId: String,
    repository: RecetarioRepository,
    onBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var recipe by remember { mutableStateOf<Recipe?>(null) }

    LaunchedEffect(recipeId) {
        recipe = repository.getRecipeById(recipeId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (recipe != null) {
            Text(
                text = "Detalles de ${recipe!!.name}",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.semantics { contentDescription = "Título de detalles de receta: ${recipe!!.name}" }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = recipe!!.getFormattedDetails(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.semantics { contentDescription = "Detalles de la receta: ${recipe!!.getFormattedDetails()}" }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onBack,
                modifier = Modifier.semantics { contentDescription = "Botón para volver" }
            ) {
                Text("Volver")
            }
        } else {
            Text(
                text = "Receta no encontrada",
                modifier = Modifier.semantics { contentDescription = "Mensaje de receta no encontrada" }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeDetailScreenPreview() {
    MaterialTheme {
        RecipeDetailScreen(
            recipeId = "1",
            repository = RecetarioRepository(
                AppDatabase.getDatabase(LocalContext.current).recipeDao(),
                AppDatabase.getDatabase(LocalContext.current).userDao(),
                AppDatabase.getDatabase(LocalContext.current).favoriteRecipeDao()
            ),
            onBack = {}
        )
    }
}