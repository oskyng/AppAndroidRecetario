package com.example.recetario.data

data class Receta(
    val name: String,
    val ingredients: List<String>,
    val instructions: String,
    val category: String
)