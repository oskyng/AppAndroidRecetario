package com.example.recetario.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.recetario.data.model.FavoriteRecipe
import com.example.recetario.data.model.Recipe
import com.example.recetario.data.model.User

@Database(entities = [Recipe::class, User::class, FavoriteRecipe::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun userDao(): UserDao
    abstract fun favoriteRecipeDao(): FavoriteRecipeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "recetario_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}