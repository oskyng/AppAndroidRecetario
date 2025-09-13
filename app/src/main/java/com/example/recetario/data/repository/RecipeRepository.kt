package com.example.recetario.data.repository

import com.example.recetario.data.model.Recipe

interface RecipeRepository {
    fun getAllRecipes(): List<Recipe>
    fun addRecipe(recipe: Recipe)
    fun updateRecipe(id: String, updatedRecipe: Recipe)
    fun deleteRecipe(id: String)
    fun findRecipeById(id: String): Recipe?
    fun filterByCategory(category: String): List<Recipe>
    fun advancedFilter(predicate: (Recipe) -> Boolean): List<Recipe>
    fun clearAllRecipes()
}