package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.recetario.data.model.ChefUser
import com.example.recetario.data.model.User
import com.example.recetario.ui.navegation.NavGraph
import com.example.recetario.ui.theme.RecetarioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecetarioTheme {
                RecetasApp()
            }
        }
    }
}

@Composable
fun RecetasApp() {
    var navController by remember { mutableStateOf<NavController?>(null) }
    var currentUser by remember { mutableStateOf<User?>(null) }

    Scaffold(
        bottomBar = {
            if (currentUser != null && navController != null) {
                BottomNavigationBar(navController!!, currentUser)
            }
        }
    ) { innerPadding ->
        NavGraph(
            modifier = Modifier.padding(innerPadding),
            currentUser = currentUser,
            onUserChange = { currentUser = it },
            onNavControllerCreated = { navController = it }
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, currentUser: User?) {
    NavigationBar {
        val currentRoute = navController.currentBackStackEntry?.destination?.route?.substringBefore("/{") ?: ""

        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Lista de Recetas") },
            label = { Text("Recetas") },
            selected = currentRoute == "recipeList",
            onClick = {
                try {
                    navController.navigate("recipeList") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                } catch (e: Exception) {
                    println("Error al navegar a recipeList: ${e.message}")
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Recetas Favoritas") },
            label = { Text("Favoritas") },
            selected = currentRoute == "favorites",
            onClick = {
                try {
                    navController.navigate("favorites") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                } catch (e: Exception) {
                    println("Error al navegar a favorites: ${e.message}")
                }
            }
        )
        if (currentUser is ChefUser) {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Add, contentDescription = "Crear Receta") },
                label = { Text("Crear") },
                selected = currentRoute == "createRecipe",
                onClick = {
                    try {
                        navController.navigate("createRecipe") {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    } catch (e: Exception) {
                        println("Error al navegar a createRecipe: ${e.message}")
                    }
                }
            )
        }
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") },
            selected = currentRoute == "profile",
            onClick = {
                try {
                    navController.navigate("profile") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                } catch (e: Exception) {
                    println("Error al navegar a profile: ${e.message}")
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Configuraciones") },
            label = { Text("Ajustes") },
            selected = currentRoute == "settings",
            onClick = {
                try {
                    navController.navigate("settings") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                } catch (e: Exception) {
                    println("Error al navegar a settings: ${e.message}")
                }
            }
        )
    }
}