package pl.wsei.pam.lab06

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import pl.wsei.pam.lab06.ui.list.ListViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ListViewModel(
                repository = todoApplication().container.todoTaskRepository
            )
        }
    }
}

fun CreationExtras.todoApplication(): TodoApplication {
    return (this[APPLICATION_KEY] as TodoApplication)
}
