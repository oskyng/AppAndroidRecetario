package com.example.recetario.data.model

import java.util.UUID

class Recipe (
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
        return timeMinutes.let { "$it minutos" }
    }
}