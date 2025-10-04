package com.example.recetario.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

class RecipeListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromRecipeList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toRecipeList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }
}

@Entity(tableName = "users")
@TypeConverters(RecipeListConverter::class)
data class User(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val firstname: String,
    val lastname: String,
    val email: String,
    val username: String,
    var password: String,
    val userType: String, // "REGULAR" o "CHEF"
    val receiveNewsletter: Boolean = false, // Solo para RegularUser
    val createdRecipes: List<String> = emptyList() // Lista de IDs de recetas (para ChefUser)
)