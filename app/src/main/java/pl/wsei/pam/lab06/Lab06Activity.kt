package pl.wsei.pam.lab06

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pl.wsei.pam.lab06.ui.theme.Lab06Theme

class Lab06Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab06Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    @Composable
    fun MainScreen() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "list") {
            composable("list") { ListScreen(navController = navController) }
            composable("form") { FormScreen(navController = navController) }
            composable("settings") { SettingsScreen(navController = navController) }
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun ListScreen(navController: NavController) {
        Scaffold(
            topBar = {
                AppTopBar(
                    navController = navController,
                    title = "Lista",
                    showBackIcon = false,
                    route = "form" // bo do formularza wracamy
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    shape = CircleShape,
                    content = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add task",
                            modifier = Modifier.scale(1.5f)
                        )
                    },
                    onClick = {
                        navController.navigate("form")
                    }
                )
            },
            content = {
                Text("Lista")
            }
        )
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun FormScreen(navController: NavController) {
        Scaffold(
            topBar = {
                AppTopBar(
                    navController = navController,
                    title = "Form",
                    showBackIcon = true,
                    route = "list"
                )
            },
            content = {
                Text("Formularz")
            }
        )
    }

    @Composable
    fun SettingsScreen(navController: NavController) {
        Text("Ekran ustawień")
    }

    @Preview(showBackground = true)
    @Composable
    fun MainScreenPreview() {
        Lab06Theme {
            MainScreen(
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppTopBar(
        navController: NavController,
        title: String,
        showBackIcon: Boolean,
        route: String
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary
            ),
            title = { Text(text = title) },
            navigationIcon = {
                if (showBackIcon) {
                    IconButton(onClick = { navController.navigate(route) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            },
            actions = {
                if (route !== "form") {
                    OutlinedButton(
                        onClick = { navController.navigate("list") }
                    )
                    {
                        Text(
                            text = "Zapisz",
                            fontSize = 18.sp
                        )
                    }
                } else {
                    IconButton(onClick = {
                        navController.navigate("settings")
                    }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                    }
                    IconButton(onClick = {
                        navController.navigate("list") {
                            popUpTo("list") { inclusive = true }
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
                    }
                }
            }
        )
    }
}