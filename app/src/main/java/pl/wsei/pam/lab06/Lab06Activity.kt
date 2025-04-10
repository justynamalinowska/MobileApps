package pl.wsei.pam.lab06

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pl.wsei.pam.lab06.ui.theme.Lab06Theme
import java.time.LocalDate

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

    enum class Priority {
        High, Medium, Low
    }

    data class TodoTask(
        val title: String,
        val deadline: LocalDate,
        val isDone: Boolean,
        val priority: Priority
    )

    fun todoTasks(): List<TodoTask> {
        return listOf(
            TodoTask("Programming", LocalDate.of(2024, 4, 18), false, Priority.Low),
            TodoTask("Teaching", LocalDate.of(2024, 5, 12), false, Priority.High),
            TodoTask("Learning", LocalDate.of(2024, 6, 28), true, Priority.Low),
            TodoTask("Cooking", LocalDate.of(2024, 8, 18), false, Priority.Medium),
        )
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

    @Composable
    fun ListItem(item: TodoTask, modifier: Modifier = Modifier) {
        ElevatedCard(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(120.dp)
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = item.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(text = "Deadline: ${item.deadline}")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Priority: ${item.priority}")
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = if (item.isDone) "Done" else "Not done")
            }
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
                LazyColumn(modifier = Modifier.padding(it)) {
                    items(items = todoTasks()) { item ->
                        ListItem(item = item)
                    }
                }
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