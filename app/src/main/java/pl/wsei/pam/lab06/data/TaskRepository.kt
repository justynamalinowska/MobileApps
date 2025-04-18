package pl.wsei.pam.lab06.data

import pl.wsei.pam.lab06.Lab06Activity.TodoTask

interface TaskRepository {
    fun getTasks(): List<TodoTask>
    fun addTask(task: TodoTask)
}