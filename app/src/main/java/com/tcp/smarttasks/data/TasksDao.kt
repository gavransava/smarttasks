package com.tcp.smarttasks.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.tcp.smarttasks.data.domain.TaskAdditionalData
import com.tcp.smarttasks.data.entity.TaskAdditionalDataEntity
import com.tcp.smarttasks.data.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TasksDao {

    @Transaction
    @Query("SELECT * FROM tasks ORDER BY priority DESC, DATE(target_date) ASC")
    abstract fun getAllTasks(): Flow<List<TaskAdditionalData>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    abstract fun getTaskById(taskId: String): TaskEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun saveTasks(tasks: List<TaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveTaskAdditionalData(data: TaskAdditionalDataEntity)

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    abstract fun getTaskWithAdditionalDataById(taskId: String): Flow<TaskAdditionalData>
}