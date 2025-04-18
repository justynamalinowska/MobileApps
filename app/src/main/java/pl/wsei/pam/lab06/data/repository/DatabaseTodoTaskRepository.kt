package pl.wsei.pam.lab06.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.wsei.pam.lab06.Lab06Activity.TodoTask
import pl.wsei.pam.lab06.data.TodoTaskDao
import pl.wsei.pam.lab06.data.TodoTaskEntity

class DatabaseTodoTaskRepository(private val dao: TodoTaskDao) : TodoTaskRepository {

    override fun getAllAsStream(): Flow<List<TodoTask>> {
        return dao.findAll().map { list ->
            list.map { it.toModel() }
        }
    }

    override fun getItemAsStream(id: Int): Flow<TodoTask?> {
        return dao.find(id).map { it?.toModel() }
    }

    override suspend fun insertItem(item: TodoTask) {
        dao.insertAll(TodoTaskEntity.fromModel(item))
    }

    override suspend fun deleteItem(item: TodoTask) {
        dao.removeById(TodoTaskEntity.fromModel(item))
    }

    override suspend fun updateItem(item: TodoTask) {
        dao.update(TodoTaskEntity.fromModel(item))
    }

    override suspend fun getAllItems(): List<TodoTask> {
        return dao.getAllTasks()
    }
}
