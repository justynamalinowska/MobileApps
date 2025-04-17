package pl.wsei.pam.lab06.ui.form

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import pl.wsei.pam.lab06.data.repository.TodoTaskRepository
import pl.wsei.pam.lab06.data.LocalDateConverter
import pl.wsei.pam.lab06.ui.list.ListViewModel
import pl.wsei.pam.lab06.ui.receiver.TaskAlarmScheduler
import todoApplication
import java.time.LocalDate

class FormViewModel(
    private val repository: TodoTaskRepository,
    private val context: Context,
    private val dateProvider: () -> LocalDate = { LocalDate.now() }
) : ViewModel() {

    var todoTaskUiState by mutableStateOf(TodoTaskUiState())
        private set

    suspend fun save(context: Context) {
        viewModelScope.launch {
            repository.insertItem(todoTaskUiState.todoTask.toTodoTask())
            val tasks = repository.getAllItems()
            val scheduler = TaskAlarmScheduler(context)
            scheduler.scheduleAlarmForNextTask(tasks)
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

    val Factory = viewModelFactory {
        initializer {
            FormViewModel(
                repository = todoApplication().container.todoTaskRepository,
                dateProvider = todoApplication().container.dateProvider::currentDate,
                context = context
            )
        }
        initializer {
            ListViewModel(
                repository = todoApplication().container.todoTaskRepository
            )
        }
    }

}