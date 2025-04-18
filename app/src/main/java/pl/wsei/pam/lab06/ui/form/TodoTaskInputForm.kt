package pl.wsei.pam.lab06.ui.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pl.wsei.pam.lab06.Lab06Activity.Priority

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoTaskInputForm(
    item: TodoTaskForm,
    modifier: Modifier = Modifier,
    onValueChange: (TodoTaskForm) -> Unit = {},
    enabled: Boolean = true
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        TextField(
            value = item.title,
            onValueChange = { onValueChange(item.copy(title = it)) },
            label = { Text("Tytuł zadania") },
            modifier = Modifier.fillMaxWidth()
        )

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = item.deadline
        )
        var showDialog by remember { mutableStateOf(false) }

        Text(
            text = "Deadline: " + java.time.Instant.ofEpochMilli(item.deadline).toString().substring(0, 10),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true }
        )

        if (showDialog) {
            DatePickerDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        datePickerState.selectedDateMillis?.let {
                            onValueChange(item.copy(deadline = it))
                        }
                    }) {
                        Text("Wybierz")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        // Priority selection
        Text("Priorytet:")
        Priority.values().forEach { priority ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = item.priority == priority.name,
                    onClick = { onValueChange(item.copy(priority = priority.name)) }
                )
                Text(priority.name)
            }
        }

        // Done checkbox
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Zakończone?")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = item.isDone,
                onCheckedChange = { onValueChange(item.copy(isDone = it)) }
            )
        }
    }
}