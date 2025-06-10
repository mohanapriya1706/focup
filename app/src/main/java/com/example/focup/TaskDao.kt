package com.example.focup

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAllTasks(): Flow<List<Task>> // Flow will emit new data automatically on change

    @Insert
    suspend fun insert(task: Task) // suspend for coroutines

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}
