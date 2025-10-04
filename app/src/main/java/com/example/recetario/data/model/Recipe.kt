package com.example.recetario.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

class ListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }
}

@Entity(tableName = "recipes")
@TypeConverters(ListConverter::class)
data class Recipe(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val category: String,
    val timeMinutes: Int
) {
    fun getFormattedDetails(): String {
        return buildString {
            append("Ingredientes:\n")
            ingredients.forEach { append("- $it\n") }
            append("\nInstrucciones:\n")
            instructions.forEachIndexed { index, step -> append("${index + 1}. $step\n") }
            append("\nTiempo de preparación: $timeMinutes minutos\n")
        }
    }

    fun getFormattedInstructions(): String {
        return buildString {
            instructions.forEachIndexed { index, step -> append("${index + 1}. $step\n") }
        }
    }

    fun getFormattedTime(): String {
        return "$timeMinutes minutos"
    }
}