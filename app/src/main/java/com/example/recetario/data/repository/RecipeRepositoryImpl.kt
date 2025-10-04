package com.example.recetario.data.repository

import com.example.recetario.data.model.Recipe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class RecipeRepositoryImpl(private val repository: RecetarioRepository): RecipeRepository {
    companion object {
        private var instance: RecipeRepositoryImpl? = null

        fun getInstance(repository: RecetarioRepository): RecipeRepositoryImpl {
            return instance ?: synchronized(this) {
                instance ?: RecipeRepositoryImpl(repository).also { instance = it }
            }
        }
    }

    override fun getAllRecipes(): List<Recipe> = runBlocking {
        repository.getAllRecipes().first()
    }

    override fun addRecipe(recipe: Recipe) {
        try {
            runBlocking {
                if (repository.getAllRecipes().first().any { it.id == recipe.id }) {
                    throw IllegalArgumentException("La receta ya existe")
                }
                repository.insertRecipe(recipe)
            }
        } catch (e: IllegalArgumentException) {
            println("Error al añadir receta: ${e.message}")
            throw e
        }
    }

    override fun updateRecipe(id: String, updatedRecipe: Recipe) {
        try {
            runBlocking {
                val existingRecipe = repository.getRecipeById(id)
                if (existingRecipe == null) throw IllegalArgumentException("Receta no encontrada")
                repository.updateRecipe(updatedRecipe.copy(id = id))
            }
        } catch (e: IllegalArgumentException) {
            println("Error al actualizar receta: ${e.message}")
            throw e
        }
    }

    override fun deleteRecipe(id: String) {
        try {
            runBlocking {
                val recipe = repository.getRecipeById(id)
                if (recipe == null) throw IllegalArgumentException("Receta no encontrada")
                repository.deleteRecipe(recipe)
            }
        } catch (e: IllegalArgumentException) {
            println("Error al eliminar receta: ${e.message}")
            throw e
        }
    }

    override fun findRecipeById(id: String): Recipe? = runBlocking {
        repository.getRecipeById(id)
    }

    override fun filterByCategory(category: String): List<Recipe> {
        try {
            if (category.isEmpty()) throw IllegalArgumentException("Categoría vacía")
            return measureTime {
                runBlocking {
                    repository.getAllRecipes().first().filter {
                        if (category == "Todas") true else it.category == category
                    }
                }
            }
        } catch (e: IllegalArgumentException) {
            println("Error: ${e.message}")
            return emptyList()
        }
    }

    override fun advancedFilter(predicate: (Recipe) -> Boolean): List<Recipe> = runBlocking {
        repository.getAllRecipes().first().filter(predicate)
    }

    override fun clearAllRecipes() {
        runBlocking {
            repository.deleteAllRecipes()
        }
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