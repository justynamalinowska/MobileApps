package pl.wsei.pam.lab06.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.wsei.pam.lab06.Priority
import java.time.LocalDate

@Entity(tableName = "tasks")
data class TodoTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val deadline: LocalDate,
    val isDone: Boolean,
    val priority: Priority
)
