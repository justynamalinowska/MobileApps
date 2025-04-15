package pl.wsei.pam.lab06.ui.form

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import pl.wsei.pam.lab06.data.repository.TodoTaskRepository
import pl.wsei.pam.lab06.data.LocalDateConverter
import java.time.LocalDate

class FormViewModel(
    private val repository: TodoTaskRepository,
    private val dateProvider: () -> LocalDate = { LocalDate.now() }
) : ViewModel() {

    var todoTaskUiState by mutableStateOf(TodoTaskUiState())
        private set

    suspend fun save() {
        if (validate()) {
            repository.insertItem(todoTaskUiState.todoTask.toTodoTask())
        }
    }

    fun updateUiState(todoTaskForm: TodoTaskForm) {
        todoTaskUiState = TodoTaskUiState(
            todoTask = todoTaskForm,
            isValid = validate(todoTaskForm)
        )
    }

    private fun validate(uiState: TodoTaskForm = todoTaskUiState.todoTask): Boolean {
        return uiState.title.isNotBlank() &&
                LocalDateConverter.fromMillis(uiState.deadline).isAfter(dateProvider())
    }
}