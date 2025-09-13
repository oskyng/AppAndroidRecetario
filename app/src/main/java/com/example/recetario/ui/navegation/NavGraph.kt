package com.example.recetario.ui.navegation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recetario.data.model.Recipe
import com.example.recetario.data.model.User
import com.example.recetario.data.repository.RecipeRepository
import com.example.recetario.data.repository.RecipeRepositoryImpl
import com.example.recetario.data.repository.UserRepository
import com.example.recetario.data.repository.UserRepositoryImpl
import com.example.recetario.ui.screens.CreateRecipeScreen
import com.example.recetario.ui.screens.FavoritesScreen
import com.example.recetario.ui.screens.LoginScreen
import com.example.recetario.ui.screens.ProfileScreen
import com.example.recetario.ui.screens.RecipeDetailScreen
import com.example.recetario.ui.screens.RecipeListScreen
import com.example.recetario.ui.screens.RecoverPasswordScreen
import com.example.recetario.ui.screens.RegisterScreen
import com.example.recetario.ui.screens.SettingsScreen
import com.example.recetario.util.SettingsState
import java.util.UUID

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    currentUser: User?,
    onUserChange: (User?) -> Unit,
    onNavControllerCreated: (NavController) -> Unit,
    settingsState: SettingsState,
    onSettingsStateChange: (SettingsState) -> Unit
) {
    val navController = rememberNavController()
    val userRepository: UserRepository = remember { UserRepositoryImpl() }
    val recipes = remember {
        mutableStateListOf<Recipe>(
            Recipe(id = UUID.randomUUID().toString(), name = "Pasta Carbonara", description = "Deliciosa pasta italiana", ingredients = listOf("Pasta", "Huevo", "Queso", "Panceta"), instructions = listOf("Cocinar pasta", "Mezclar huevos y queso", "Añadir panceta"), category = "Almuerzo", timeMinutes = 20),
            Recipe(id = UUID.randomUUID().toString(), name = "Ensalada César", description = "Ensalada fresca con aderezo", ingredients = listOf("Lechuga", "Pollo", "Croutons", "Aderezo"), instructions = listOf("Lavar lechuga", "Cocinar pollo", "Mezclar aderezo"), category = "Ensalada", timeMinutes = 15),
            Recipe(id = UUID.randomUUID().toString(), name = "Tacos al Pastor", description = "Tacos mexicanos tradicionales", ingredients = listOf("Cerdo", "Piña", "Cilantro", "Cebolla"), instructions = listOf("Marinar cerdo", "Asar carne", "Servir con piña"), category = "Almuerzo", timeMinutes = 30),
            Recipe(id = UUID.randomUUID().toString(), name = "Panqueques", description = "Desayuno esponjoso", ingredients = listOf("Harina", "Leche", "Huevos"), instructions = listOf("Mezclar ingredientes", "Cocinar en sartén"), category = "Desayuno", timeMinutes = 10),
            Recipe(id = UUID.randomUUID().toString(), name = "Tarta de Manzana", description = "Postre clásico", ingredients = listOf("Manzanas", "Masa", "Azúcar"), instructions = listOf("Preparar masa", "Cortar manzanas", "Hornear"), category = "Postre", timeMinutes = 45)
        )
    }
    val favoriteRecipes = remember { mutableStateListOf<Recipe>() }
    val recipeRepository: RecipeRepository = remember { RecipeRepositoryImpl(recipes) }

    LaunchedEffect(navController) {
        onNavControllerCreated(navController)
    }

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN,
        modifier = modifier
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { user ->
                    onUserChange(user)
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Routes.REGISTER) },
                onRecoverPasswordClick = { navController.navigate(Routes.FORGOT) },
                userRepository = userRepository
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(Routes.LOGIN) },
                onBackToLogin = { navController.navigate(Routes.LOGIN) },
                userRepository = userRepository
            )
        }
        composable(Routes.FORGOT) {
            RecoverPasswordScreen(
                userRepository = userRepository,
                onBackToLogin = { navController.navigate(Routes.LOGIN) }
            )
        }
        composable(Routes.HOME) {
            RecipeListScreen(
                repository = recipeRepository,
                favoriteRecipes = favoriteRecipes,
                onAddToFavorites = { recipe -> favoriteRecipes.add(recipe) },
                onRemoveFromFavorites = { recipe -> favoriteRecipes.remove(recipe) },
                onNavigateToDetail = { recipeId ->
                    navController.navigate("recipe_detail/$recipeId")
                }
            )
        }
        composable(
            Routes.RECIPE_DETAIL,
            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
            val recipe = recipeRepository.findRecipeById(recipeId)
            RecipeDetailScreen(
                recipe = recipe,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.PROFILE) {
            ProfileScreen(
                user = currentUser,
                onLogout = {
                    onUserChange(null)
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                settingsState = settingsState,
                onSettingsStateChange = onSettingsStateChange
            )
        }
        composable(Routes.FAVORITE_RECIPES) {
            FavoritesScreen(
                favoriteRecipes = favoriteRecipes,
                onRemoveFromFavorites = { recipe -> favoriteRecipes.remove(recipe) }
            )
        }
        composable(Routes.CREATE_RECIPE) {
            CreateRecipeScreen(
                user = currentUser,
                recipeRepository = recipeRepository,
                onRecipeCreated = { navController.navigate(Routes.HOME) }
            )
        }
    }
}