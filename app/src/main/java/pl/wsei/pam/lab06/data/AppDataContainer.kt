package pl.wsei.pam.lab06.data
import android.content.Context

class AppDataContainer(private val context: Context) : AppContainer {
    override val taskRepository: TaskRepository by lazy {
        InMemoryTaskRepository()
    }
}