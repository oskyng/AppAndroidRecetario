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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recetario.data.db.AppDatabase
import com.example.recetario.data.model.Recipe
import com.example.recetario.data.model.User
import com.example.recetario.data.repository.RecetarioRepository
import com.example.recetario.data.repository.RecipeRepositoryImpl
import kotlinx.coroutines.launch

@Composable
fun FavoritesScreen(
    repository: RecetarioRepository,
    currentUser: User?,
    onNavigateToDetail: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val favoriteRecipes by currentUser?.let { repository.getFavoriteRecipes(it.id) }?.collectAsState(initial = emptyList()) ?: mutableStateOf(emptyList())

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
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenPreview() {
    MaterialTheme {
        FavoritesScreen(
            repository = RecetarioRepository(AppDatabase.getDatabase(androidx.compose.ui.platform.LocalContext.current).recipeDao(), AppDatabase.getDatabase(androidx.compose.ui.platform.LocalContext.current).userDao(), AppDatabase.getDatabase(androidx.compose.ui.platform.LocalContext.current).favoriteRecipeDao()),
            currentUser = null,
            onNavigateToDetail = {}
        )
    }
}