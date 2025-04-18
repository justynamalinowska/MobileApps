package pl.wsei.pam.lab06.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pl.wsei.pam.lab06.Lab06Activity.TodoTask
import pl.wsei.pam.lab06.data.repository.TodoTaskRepository

data class ListUiState(val items: List<TodoTask> = listOf())

class ListViewModel(
    private val repository: TodoTaskRepository
) : ViewModel() {

    val listUiState: StateFlow<ListUiState> = repository
        .getAllAsStream()
        .map { ListUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ListUiState()
        )

    fun addTask(task: TodoTask) {
        viewModelScope.launch {
            repository.insertItem(task)
        }
    }
}