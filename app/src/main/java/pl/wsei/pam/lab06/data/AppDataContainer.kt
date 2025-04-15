package pl.wsei.pam.lab06.data

import android.content.Context
import pl.wsei.pam.lab06.data.AppDatabase
import pl.wsei.pam.lab06.data.repository.DatabaseTodoTaskRepository
import pl.wsei.pam.lab06.data.repository.TodoTaskRepository

class AppDataContainer(private val context: Context): AppContainer {

    val database by lazy {
        AppDatabase.getInstance(context)
    }

    override val todoTaskRepository: TodoTaskRepository by lazy {
        DatabaseTodoTaskRepository(database.taskDao())
    }
}
