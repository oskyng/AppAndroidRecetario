package com.example.recetario.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Query
import com.example.recetario.data.model.FavoriteRecipe
import com.example.recetario.data.model.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRecipeDao {
    @Query("SELECT recipes.* FROM recipes INNER JOIN favorite_recipes ON recipes.id = favorite_recipes.recipeId WHERE favorite_recipes.userId = :userId")
    fun getFavoriteRecipes(userId: String): Flow<List<Recipe>>

    @Insert
    suspend fun addFavorite(favorite: FavoriteRecipe)

    @Delete
    suspend fun removeFavorite(favorite: FavoriteRecipe)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE userId = :userId AND recipeId = :recipeId)")
    suspend fun isFavorite(userId: String, recipeId: String): Boolean

    @Query("DELETE FROM favorite_recipes WHERE userId = :userId")
    suspend fun clearFavorites(userId: String)
}