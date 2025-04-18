package pl.wsei.pam.lab06.data

import pl.wsei.pam.lab06.Lab06Activity.TodoTask

class InMemoryTaskRepository : TaskRepository {
    private val taskList = mutableListOf<TodoTask>()

    override fun getTasks(): List<TodoTask> = taskList

    override fun addTask(task: TodoTask) {
        taskList.add(task)
    }
}