package com.example.recetario.data.repository

import com.example.recetario.data.model.Recipe

class RecipeRepositoryImpl(private val recipes: MutableList<Recipe>): RecipeRepository {
    companion object {
        val INSTANCE = RecipeRepositoryImpl(mutableListOf())
    }

    override fun getAllRecipes(): List<Recipe> = recipes

    override fun clearAllRecipes() {
        recipes.clear()
    }

    override fun addRecipe(recipe: Recipe) {
        try {
            if (recipes.any { it.id == recipe.id }) {
                throw IllegalArgumentException("La receta ya existe")
            }
            recipes.addIfNotExists(recipe)
        } catch (e: IllegalArgumentException) {
            println("Error al añadir receta: ${e.message}")
            throw e
        }
    }

    override fun updateRecipe(id: String, updatedRecipe: Recipe) {
        try {
            val index = recipes.indexOfFirst { it.id == id }
            if (index == -1) throw IllegalArgumentException("Receta no encontrada")
            recipes[index] = updatedRecipe
        } catch (e: IllegalArgumentException) {
            println("Error al actualizar receta: ${e.message}")
            throw e
        }
    }

    override fun deleteRecipe(id: String) {
        try {
            val recipe = recipes.find { it.id == id }
            if (recipe == null || !recipes.remove(recipe)) {
                throw IllegalArgumentException("Receta no encontrada")
            }
        } catch (e: IllegalArgumentException) {
            println("Error al eliminar receta: ${e.message}")
            throw e
        }
    }

    override fun findRecipeById(id: String): Recipe? {
        var index = 0
        while (index < recipes.size) {
            if (recipes[index].id == id) return recipes[index]
            index++
        }
        return null
    }

    override fun filterByCategory(category: String): List<Recipe> {
        try {
            if (category.isEmpty()) throw IllegalArgumentException("Categoría vacía")
        } catch (e: IllegalArgumentException) {
            println("Error: ${e.message}")
            return emptyList()
        }

        val filtered = recipes.filter { if (category == "Todas") true else it.category == category }

        filtered.forEach outer@{ recipe ->
            if (recipe.name.isEmpty()) return@outer
            println(recipe.name)
        }

        return measureTime { filtered }
    }

    override fun advancedFilter(predicate: (Recipe) -> Boolean): List<Recipe> {
        return recipes.filter(predicate)
    }

    inline fun <reified T> measureTime(block: () -> T): T {
        val start = System.currentTimeMillis()
        val result = block()
        val end = System.currentTimeMillis()
        println("Tiempo de ejecución: ${end - start} ms")
        return result
    }
}

// Propiedad de extensión
val List<Recipe>.totalRecipes: Int
    get() = size

// Función de extensión
fun MutableList<Recipe>.addIfNotExists(recipe: Recipe) {
    if (!contains(recipe)) add(recipe)
}