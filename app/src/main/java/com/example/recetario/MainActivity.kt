package com.example.recetario

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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.recetario.data.db.AppDatabase
import com.example.recetario.data.model.User
import com.example.recetario.data.repository.RecetarioRepository
import com.example.recetario.ui.navegation.NavGraph
import com.example.recetario.ui.theme.RecetarioTheme
import com.example.recetario.util.SettingsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var settingsState by remember { mutableStateOf(SettingsState()) }
            var navController by remember { mutableStateOf<NavController?>(null) }
            var currentUser by remember { mutableStateOf<User?>(null) }
            val context = LocalContext.current
            val repository = remember {
                RecetarioRepository(
                    AppDatabase.getDatabase(context).recipeDao(),
                    AppDatabase.getDatabase(context).userDao(),
                    AppDatabase.getDatabase(context).favoriteRecipeDao()
                )
            }

            RecetarioTheme(
                settingsState = settingsState
            ) {
                Scaffold(
                    bottomBar = {
                        if (currentUser != null && navController != null) {
                            BottomNavigationBar(
                                navController = navController!!,
                                currentUser = currentUser,
                                isSimplifiedNavigation = settingsState.isSimplifiedNavigation,
                                isVerboseTalkBack = settingsState.isVerboseTalkBack,
                                iconScale = settingsState.iconScale
                            )
                        }
                    }
                ) { innerPadding ->
                    NavGraph(
                        modifier = Modifier.padding(innerPadding),
                        currentUser = currentUser,
                        onUserChange = { currentUser = it },
                        onNavControllerCreated = { navController = it },
                        settingsState = settingsState,
                        onSettingsStateChange = { settingsState = it }
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentUser: User?,
    isSimplifiedNavigation: Boolean,
    isVerboseTalkBack: Boolean,
    iconScale: Float
) {
    NavigationBar {
        val currentRoute = navController.currentBackStackEntry?.destination?.route?.substringBefore("/{") ?: ""

        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.List,
                    contentDescription = if (isVerboseTalkBack) "Navegar a la lista de recetas" else "Lista de Recetas",
                    modifier = Modifier.scale(iconScale)
                )
            },
            label = { Text("Recetas") },
            selected = currentRoute == "recipe",
            onClick = {
                try {
                    navController.navigate("recipe") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                } catch (e: Exception) {
                    println("Error al navegar a recipe: ${e.message}")
                }
            }
        )

        if (!isSimplifiedNavigation) {
            NavigationBarItem(
                icon = {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = if (isVerboseTalkBack) "Navegar a las recetas favoritas" else "Recetas Favoritas",
                        modifier = Modifier.scale(iconScale)
                    )
                },
                label = { Text("Favoritas") },
                selected = currentRoute == "favorite_recipes",
                onClick = {
                    try {
                        navController.navigate("favorite_recipes") {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    } catch (e: Exception) {
                        println("Error al navegar a favorite_recipes: ${e.message}")
                    }
                }
            )
        }

        if (currentUser?.userType == "CHEF" && !isSimplifiedNavigation) {
            NavigationBarItem(
                icon = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = if (isVerboseTalkBack) "Navegar a crear una nueva receta" else "Crear Receta",
                        modifier = Modifier.scale(iconScale)
                    )
                },
                label = { Text("Crear") },
                selected = currentRoute == "create_recipe",
                onClick = {
                    try {
                        navController.navigate("create_recipe") {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    } catch (e: Exception) {
                        println("Error al navegar a create_recipe: ${e.message}")
                    }
                }
            )
        }

        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = if (isVerboseTalkBack) "Navegar al perfil del usuario" else "Perfil",
                    modifier = Modifier.scale(iconScale)
                )
            },
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

        if (!isSimplifiedNavigation) {
            NavigationBarItem(
                icon = {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = if (isVerboseTalkBack) "Navegar a la pantalla de configuraciones" else "Configuraciones",
                        modifier = Modifier.scale(iconScale)
                    )
                },
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
}