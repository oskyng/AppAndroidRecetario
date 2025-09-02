package com.example.recetario.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recetario.data.Receta
import com.example.recetario.ui.common.RecipeCard
import com.example.recetario.ui.common.SimpleTopBar
import com.example.recetario.util.SharedState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val listOfRecipe = listOf(
        Receta(
            name = "Tostada con Palta",
            ingredients = listOf("1 rebanada de pan", "1/2 palta", "Sal", "Pimienta"),
            instructions = "1. Tostar el pan.\n2. Machacar el palta y untarlo en el pan.\n3. Sazonar con sal y pimienta al gusto.",
            category = "Desayuno"
        ),
        Receta(
            name = "Batido de Frutas",
            ingredients = listOf("1 plátano", "1/2 taza de fresas", "1/2 taza de leche", "1 cucharada de miel (opcional)"),
            instructions = "1. Colocar todos los ingredientes en una licuadora.\n2. Licuar hasta obtener una mezcla homogénea.\n3. Servir inmediatamente.",
            category = "Postre"
        ),
        Receta(
            name = "Ensalada César Simple",
            ingredients = listOf("Lechuga romana", "Crutones", "Queso parmesano rallado", "Aderezo César"),
            instructions = "1. Lavar y cortar la lechuga.\n2. Mezclar la lechuga con los crutones y el queso parmesano.\n3. Añadir el aderezo César y mezclar bien.",
            category = "Ensalada"
        ),
        Receta(
            name = "Huevos Revueltos",
            ingredients = listOf("2 huevos", "1 cucharada de leche", "Sal", "Pimienta", "Mantequilla o aceite"),
            instructions = "1. Batir los huevos con la leche, sal y pimienta.\n2. Calentar la mantequilla o aceite en una sartén.\n3. Verter la mezcla de huevo y cocinar, revolviendo ocasionalmente, hasta que estén cocidos.",
            category = "Desayuno"
        ),
        Receta(
            name = "Avena Nocturna",
            ingredients = listOf("1/2 taza de avena", "1 taza de leche (o alternativa)", "1 cucharada de semillas de chía", "Fruta al gusto"),
            instructions = "1. En un frasco, mezclar la avena, la leche y las semillas de chía.\n2. Refrigerar durante la noche (o al menos 4 horas).\n3. Antes de servir, añadir tu fruta favorita.",
            category = "Postre"
        )
    )

    Scaffold (
        topBar = { SimpleTopBar(
            title = "Recetas",
            showBack = false,
            onMenu = {
                scope.launch {
                    drawerState.open()
                }
            },
            onSettings = { }
        ) },
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
        padding -> Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RecipeListUI(recipes = listOfRecipe)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListUI(recipes: List<Receta>) {
    var selectedCategory by remember { mutableStateOf("Todas") }
    val categories = listOf("Todas", "Desayuno", "Almuerzo", "Ensalada", "Postre")
    val fontSize by remember { derivedStateOf { SharedState.currentFontSize.sp } }
    var expanded by remember { mutableStateOf(false) }

    Text(
        text = "Lista de Recetas",
        style = MaterialTheme.typography.headlineMedium.copy(fontSize = fontSize * 1.5f),
        color = Color.Black,
        modifier = Modifier.semantics { contentDescription = "Subtítulo: Lista de Recetas" }
    )
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .padding(16.dp)
            .semantics { contentDescription = "Menú de categorías de recetas" }
    ) {
        OutlinedTextField(
            value = selectedCategory,
            onValueChange = {},
            readOnly = true,
            label = { Text("Categoría") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .semantics { contentDescription = "Seleccionar categoría de recetas" }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        selectedCategory = category
                        expanded = false
                    },
                    modifier = Modifier.semantics { contentDescription = "Opción de categoría: $category" }
                )
            }
        }
    }

    val filteredRecipes = when (selectedCategory) {
        "Todas" -> recipes
        else -> recipes.filter { it.category == selectedCategory }
    }

    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(filteredRecipes) { recipe -> RecipeCard( recipe = recipe, fontSize = fontSize) }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreviewMain() {
    MaterialTheme { MainApp() }
}