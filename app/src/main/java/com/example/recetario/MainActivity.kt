package com.example.recetario


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                MainApp()
            }
        }
    }
}

data class Receta(
    val name: String,
    val ingredients: List<String>,
    val instructions: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val listOfRecipe = listOf(
        Receta(
            name = "Tostada con Aguacate",
            ingredients = listOf("1 rebanada de pan", "1/2 aguacate", "Sal", "Pimienta"),
            instructions = "1. Tostar el pan.\n2. Machacar el aguacate y untarlo en el pan.\n3. Sazonar con sal y pimienta al gusto."
        ),
        Receta(
            name = "Batido de Frutas",
            ingredients = listOf("1 plátano", "1/2 taza de fresas", "1/2 taza de leche", "1 cucharada de miel (opcional)"),
            instructions = "1. Colocar todos los ingredientes en una licuadora.\n2. Licuar hasta obtener una mezcla homogénea.\n3. Servir inmediatamente."
        ),
        Receta(
            name = "Ensalada César Simple",
            ingredients = listOf("Lechuga romana", "Crutones", "Queso parmesano rallado", "Aderezo César"),
            instructions = "1. Lavar y cortar la lechuga.\n2. Mezclar la lechuga con los crutones y el queso parmesano.\n3. Añadir el aderezo César y mezclar bien."
        ),
        Receta(
            name = "Huevos Revueltos",
            ingredients = listOf("2 huevos", "1 cucharada de leche", "Sal", "Pimienta", "Mantequilla o aceite"),
            instructions = "1. Batir los huevos con la leche, sal y pimienta.\n2. Calentar la mantequilla o aceite en una sartén.\n3. Verter la mezcla de huevo y cocinar, revolviendo ocasionalmente, hasta que estén cocidos."
        ),
        Receta(
            name = "Avena Nocturna",
            ingredients = listOf("1/2 taza de avena", "1 taza de leche (o alternativa)", "1 cucharada de semillas de chía", "Fruta al gusto"),
            instructions = "1. En un frasco, mezclar la avena, la leche y las semillas de chía.\n2. Refrigerar durante la noche (o al menos 4 horas).\n3. Antes de servir, añadir tu fruta favorita."
        )
    )

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text("Recetario", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Filled.Settings, contentDescription = "Ajustes")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp
                ),
                shape = MaterialTheme.shapes.large
            ) { Icon(Icons.Filled.Add, contentDescription = "Incrementar") }
        }
    ) {
        innerPadding -> RecipeListUI(recipes = listOfRecipe, contentPadding = innerPadding)
    }

}

@Composable
fun RecipeCard(recipe: Receta) {
    Card (
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column (
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Ingredientes:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            recipe.ingredients.forEach { ingredient ->
                Text(
                    text = "- $ingredient",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Instrucciones:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = recipe.instructions,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun RecipeListUI(recipes: List<Receta>, contentPadding: PaddingValues) {
    LazyColumn (
        modifier = Modifier.fillMaxSize().padding(contentPadding).padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(recipes) { recipe -> RecipeCard( recipe = recipe) }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreviewMain() {
    MaterialTheme { MainApp() }
}