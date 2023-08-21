package com.example.to_docompose.data

import androidx.room.*
import com.example.to_docompose.data.models.ToDoTask
import com.example.to_docompose.util.Constants.DATABASE_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Query(value = "SELECT * FROM $DATABASE_TABLE ORDER BY id ASK")
    fun getAllTasks(): Flow<List<ToDoTask>>

    @Query(value = "SELECT * FROM $DATABASE_TABLE WHERE id=:taskId")
    fun getSelectedTask(taskId: Int): Flow<ToDoTask>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(toDoTask: ToDoTask)

    @Update
    suspend fun updateTask(toDoTask: ToDoTask)

    @Delete
    suspend fun deleteTask(toDoTask: ToDoTask)

    @Query(value = "DELETE FROM $DATABASE_TABLE")
    suspend fun deleteAllTasks()

    @Query(value = "SELECT * FROM $DATABASE_TABLE WHERE title LIKE :query OR description LIKE :query")
    fun searchDatabase(query: String): Flow<List<ToDoTask>>

    @Query(value = "SELECT * FROM $DATABASE_TABLE ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): Flow<List<ToDoTask>>

    @Query(value = "SELECT * FROM $DATABASE_TABLE ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): Flow<List<ToDoTask>>
}