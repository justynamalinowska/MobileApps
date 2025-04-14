package pl.wsei.pam.lab06.data

interface AppContainer {
    val database: AppDatabase
    val todoTaskDao: TodoTaskDao
}