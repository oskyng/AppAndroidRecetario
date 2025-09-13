package com.example.recetario.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.recetario.data.model.Recipe

@Composable
fun FavoritesScreen(
    favoriteRecipes: List<Recipe>,
    onRemoveFromFavorites: (Recipe) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Recetas Favoritas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.semantics { contentDescription = "Título de la pantalla de recetas favoritas" }
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (favoriteRecipes.isEmpty()) {
            Text(
                text = "No tienes recetas favoritas",
                modifier = Modifier.semantics { contentDescription = "Mensaje de recetas favoritas vacías" }
            )
        } else {
            LazyColumn {
                items(favoriteRecipes) { recipe ->
                    RecipeItem(
                        recipe = recipe,
                        isFavorite = true,
                        onAddToFavorites = { },
                        onRemoveFromFavorites = { onRemoveFromFavorites(recipe) },
                        onClick = { }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}