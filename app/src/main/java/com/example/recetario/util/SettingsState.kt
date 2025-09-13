package com.example.recetario.util

data class SettingsState(
    val isDark: Boolean = false,
    val fontScale: Float = 1.0f,
    val isHighContrast: Boolean = false,
    val isSimplifiedNavigation: Boolean = false,
    val isVerboseTalkBack: Boolean = false,
    val iconScale: Float = 1.0f
)