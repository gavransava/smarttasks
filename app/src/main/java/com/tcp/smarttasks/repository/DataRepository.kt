package com.tcp.smarttasks.repository

import com.tcp.smarttasks.data.domain.Task
import com.tcp.smarttasks.data.domain.TaskStatus
import com.tcp.smarttasks.network.Resource
import kotlinx.coroutines.flow.Flow

interface DataRepository {

    fun getAllTasks(): Flow<List<Task>>
    fun fetchTasks(): Flow<Resource<List<Task>>>
    fun getTask(taskId: String): Flow<Task>

    suspend fun setTaskStatus(taskId: String, status: TaskStatus)
}