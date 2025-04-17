package pl.wsei.pam.lab06.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.wsei.pam.lab06.Lab06Activity.TodoTask

@Dao
interface TodoTaskDao {

    @Insert
    suspend fun insertAll(vararg tasks: TodoTaskEntity)

    @Update
    suspend fun update(task: TodoTaskEntity)

    @Delete
    suspend fun removeById(task: TodoTaskEntity)

    @Query("SELECT * FROM tasks ORDER BY deadline DESC")
    fun findAll(): Flow<List<TodoTaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun find(id: Int): Flow<TodoTaskEntity?>

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<TodoTask>
}
