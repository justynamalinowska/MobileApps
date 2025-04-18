import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import pl.wsei.pam.lab06.TodoApplication
import pl.wsei.pam.lab06.ui.form.FormViewModel
import pl.wsei.pam.lab06.ui.list.ListViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val context = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]!!.applicationContext
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

fun CreationExtras.todoApplication(): TodoApplication {
    return (this[APPLICATION_KEY] as TodoApplication)
}
