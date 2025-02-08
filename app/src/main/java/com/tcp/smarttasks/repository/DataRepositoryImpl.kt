package com.tcp.smarttasks.repository

import com.tcp.smarttasks.data.TasksDao
import com.tcp.smarttasks.data.domain.Task
import com.tcp.smarttasks.data.domain.TaskStatus
import com.tcp.smarttasks.data.entity.TaskAdditionalDataEntity
import com.tcp.smarttasks.data.mapper.toDomain
import com.tcp.smarttasks.data.mapper.toEntity
import com.tcp.smarttasks.network.Resource
import com.tcp.smarttasks.network.TasksService
import com.tcp.smarttasks.util.NetworkConnectivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DataRepositoryImpl @Inject constructor(
    private val networkConnectivity: NetworkConnectivity,
    private val tasksService: TasksService,
    private val ioDispatcher: CoroutineContext,
    private val tasksDao: TasksDao
) : DataRepository {

    override fun getAllTasks(): Flow<List<Task>> {
        return tasksDao.getAllTasks().map { taskEntities -> taskEntities.map { it.toDomain() } }
    }

    override fun fetchTasks(): Flow<Resource<List<Task>>> {
        return flow {
            if (!networkConnectivity.isConnected()) {
                emit(Resource.Error("No Internet Connection"))
                return@flow
            }

            emit(Resource.Loading)

            try {
                val response = tasksService.fetchTasks()
                when {
                    response.isSuccessful -> {
                        val networkTasks = response.body()?.tasks?.filter { it.targetDate != null && it.title != null } ?: emptyList()
                        if (networkTasks.isNotEmpty()) {
                            val tasks = networkTasks.map { it.toDomain() }
                            tasksDao.saveTasks(tasks.map { it.toEntity() })
                            emit(Resource.Success(tasks))
                        } else {
                            emit(Resource.Error("Empty response body"))
                        }
                    }
                    else -> {
                        emit(Resource.Error("Error: ${response.message()}"))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unknown Error Occurred"))
            }
        }.flowOn(ioDispatcher)
    }

    override fun getTask(taskId: String): Flow<Task> {
        return tasksDao.getTaskWithAdditionalDataById(taskId)
            .map { taskAdditionalData -> taskAdditionalData.toDomain() }
    }

    override suspend fun setTaskStatus(taskId: String, status: TaskStatus, comment: String) {
        tasksDao.saveTaskAdditionalData(
            TaskAdditionalDataEntity(
                taskId = taskId,
                taskStatus = status,
                userComment = comment
            )
        )
    }
}
