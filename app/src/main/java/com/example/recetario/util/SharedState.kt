package com.example.recetario.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.recetario.data.Usuario

object SharedState {
    val users = mutableStateListOf<Usuario>()
    val fontSizes = listOf(14f, 16f, 18f, 20f, 22f)
    var currentFontSizeIndex by mutableStateOf(1)
    val currentFontSize: Float
        get() = fontSizes[currentFontSizeIndex]

    fun increaseFontSize() {
        if (currentFontSizeIndex < fontSizes.size - 1) {
            currentFontSizeIndex++
        }
    }

    fun decreaseFontSize() {
        if (currentFontSizeIndex > 0) {
            currentFontSizeIndex--
        }
    }
}