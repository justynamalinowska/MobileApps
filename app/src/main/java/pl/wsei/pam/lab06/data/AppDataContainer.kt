package pl.wsei.pam.lab06.data

import android.content.Context

class AppDataContainer(context: Context): AppContainer {
    override val database: AppDatabase = AppDatabase.getInstance(context)
    override val todoTaskDao: TodoTaskDao = database.taskDao()
}
