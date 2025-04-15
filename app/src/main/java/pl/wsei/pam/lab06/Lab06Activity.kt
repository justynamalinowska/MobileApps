package pl.wsei.pam.lab06

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

// --- DATA STRUCTURES ---
enum class Priority {
    High, Medium, Low
}

data class TodoTask(
    val id: Int = 0,
    val title: String,
    val deadline: LocalDate,
    val isDone: Boolean,
    val priority: Priority
)

val taskList = mutableStateListOf(
    TodoTask(0, "Programming", LocalDate.of(2024, 4, 18), false, Priority.Low),
    TodoTask(1, "Teaching", LocalDate.of(2024, 5, 12), false, Priority.High),
    TodoTask(2, "Learning", LocalDate.of(2024, 6, 28), true, Priority.Low),
    TodoTask(3, "Cooking", LocalDate.of(2024, 8, 18), false, Priority.Medium)
)

// --- ACTIVITY ---
class Lab06Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab06Theme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen()
                }
            }
        }
    }
}

// --- SCREENS ---
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "list") {
        composable("list") { ListScreen(navController) }
        composable("form") { FormScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
    }
}

@Composable
fun ListScreen(navController: NavController) {
    Scaffold(
        topBar = {
            AppTopBar(
                navController = navController,
                title = "Lista",
                showBackIcon = false,
                onSaveClick = null
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = { navController.navigate("form") }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add", modifier = Modifier.scale(1.5f))
            }
        },
        content = {
            LazyColumn(modifier = Modifier.padding(it)) {
                items(taskList) { task ->
                    ListItem(task)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(navController: NavController) {
    var title by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedPriority by remember { mutableStateOf(Priority.Low) }
    var isDone by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        selectedDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Anuluj") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                navController = navController,
                title = "Formularz",
                showBackIcon = true,
                onSaveClick = {
                    if (title.isNotBlank() && selectedDate != null) {
                        val newId = (taskList.maxOfOrNull { it.id } ?: 0) + 1
                        taskList.add(
                            TodoTask(
                                id = newId,
                                title = title,
                                deadline = selectedDate!!,
                                isDone = isDone,
                                priority = selectedPriority
                            )
                        )
                        navController.navigate("list") {
                            popUpTo("list") { inclusive = true }
                        }
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Tytuł zadania") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(onClick = { showDatePicker = true }) {
                    Text(text = selectedDate?.toString() ?: "Wybierz datę")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Priorytet:")

                DropdownMenuBox(
                    options = Priority.values().toList(),
                    selected = selectedPriority,
                    onSelectedChange = { selectedPriority = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Zakończone?")
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(checked = isDone, onCheckedChange = { isDone = it })
                }
            }
        }
    )
}

@Composable
fun SettingsScreen(navController: NavController) {
    Text("Ekran ustawień")
}

@Composable
fun ListItem(item: TodoTask, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(120.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text("Deadline: ${item.deadline}")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Priority: ${item.priority}")
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(if (item.isDone) "Done" else "Not done")
        }
    }
}

@Composable
fun DropdownMenuBox(options: List<Priority>, selected: Priority, onSelectedChange: (Priority) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        OutlinedButton(onClick = { expanded = true }) {
            Text(text = selected.name)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { priority ->
                DropdownMenuItem(
                    text = { Text(priority.name) },
                    onClick = {
                        onSelectedChange(priority)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    navController: NavController,
    title: String,
    showBackIcon: Boolean,
    onSaveClick: (() -> Unit)? = null
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = { Text(text = title) },
        navigationIcon = {
            if (showBackIcon) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            if (onSaveClick != null) {
                OutlinedButton(onClick = onSaveClick) {
                    Text("Zapisz", fontSize = 18.sp)
                }
            } else {
                IconButton(onClick = { navController.navigate("settings") }) {
                    Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings")
                }
                IconButton(onClick = { navController.navigate("form") }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    Lab06Theme {
        MainScreen()
    }
}
