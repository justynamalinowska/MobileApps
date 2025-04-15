package pl.wsei.pam.lab06.data

import pl.wsei.pam.lab06.TodoTask

interface TaskRepository {
    fun getTasks(): List<TodoTask>
    fun addTask(task: TodoTask)
}