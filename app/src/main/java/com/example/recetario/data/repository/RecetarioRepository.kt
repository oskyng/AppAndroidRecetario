package com.example.recetario.data.repository

import com.example.recetario.data.db.FavoriteRecipeDao
import com.example.recetario.data.db.RecipeDao
import com.example.recetario.data.db.UserDao
import com.example.recetario.data.model.FavoriteRecipe
import com.example.recetario.data.model.Recipe
import com.example.recetario.data.model.User
import kotlinx.coroutines.flow.Flow

class RecetarioRepository(
    private val recipeDao: RecipeDao,
    private val userDao: UserDao,
    private val favoriteRecipeDao: FavoriteRecipeDao
) {
    // Recipes
    fun getAllRecipes(): Flow<List<Recipe>> = recipeDao.getAllRecipes()

    suspend fun getRecipeById(id: String): Recipe? = recipeDao.getRecipeById(id)

    suspend fun insertRecipe(recipe: Recipe) = recipeDao.insertRecipe(recipe)

    suspend fun insertAllRecipes(recipes: List<Recipe>) = recipeDao.insertAll(recipes)

    suspend fun updateRecipe(recipe: Recipe) = recipeDao.updateRecipe(recipe)

    suspend fun deleteRecipe(recipe: Recipe) = recipeDao.deleteRecipe(recipe)

    suspend fun deleteAllRecipes() = recipeDao.deleteAll()

    // Users
    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()

    fun getUsersByType(type: String): Flow<List<User>> = userDao.getUsersByType(type)

    suspend fun getUserById(id: String): User? = userDao.getUserById(id)

    suspend fun insertUser(user: User) = userDao.insertUser(user)

    suspend fun insertAllUsers(users: List<User>) = userDao.insertAll(users)

    suspend fun updateUser(user: User) = userDao.updateUser(user)

    suspend fun deleteUser(user: User) = userDao.deleteUser(user)

    suspend fun deleteAllUsers() = userDao.deleteAll()

    // Favorites
    fun getFavoriteRecipes(userId: String): Flow<List<Recipe>> = favoriteRecipeDao.getFavoriteRecipes(userId)

    suspend fun addFavorite(userId: String, recipeId: String) = favoriteRecipeDao.addFavorite(
        FavoriteRecipe(userId, recipeId)
    )

    suspend fun removeFavorite(userId: String, recipeId: String) = favoriteRecipeDao.removeFavorite(FavoriteRecipe(userId, recipeId))

    suspend fun isFavorite(userId: String, recipeId: String): Boolean = favoriteRecipeDao.isFavorite(userId, recipeId)

    suspend fun clearFavorites(userId: String) = favoriteRecipeDao.clearFavorites(userId)
}