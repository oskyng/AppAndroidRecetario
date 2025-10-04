package com.example.recetario.data.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "favorite_recipes",
    primaryKeys = ["userId", "recipeId"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Recipe::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FavoriteRecipe(
    val userId: String,
    val recipeId: String
)