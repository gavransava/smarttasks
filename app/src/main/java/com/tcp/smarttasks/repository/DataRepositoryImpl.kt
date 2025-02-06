package com.tcp.smarttasks.repository

import com.tcp.smarttasks.data.TasksDao
import com.tcp.smarttasks.data.domain.Task
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

    fun getAllTasks(): Flow<List<Task>> {
        return tasksDao.getAllTasks().map { taskEntities -> taskEntities.map { it.toDomain() } }
    }

    override fun fetchTasks(): Flow<Resource<List<Task>>> {
        return flow {
            if (!networkConnectivity.isConnected()) {
                emit(Resource.Error("No Internet Connection"))
            } else {
                emit(Resource.Loading)
                try {
                    val response = tasksService.fetchTasks()
                    if (response.isSuccessful) {
                        response.body()?.let { networkTasks ->
                            val tasks = networkTasks.tasks.filter {
                                it.targetDate != null && it.title != null && it.description != null
                            }.map { it.toDomain() }

                            tasksDao.saveTasks(tasks.map { it.toEntity() })
                            emit(Resource.Success(tasks))
                        } ?: emit(Resource.Error("Empty response body"))
                    } else {
                        emit(Resource.Error("Error: ${response.message()}"))
                    }
                } catch (e: Exception) {
                    emit(Resource.Error(e.message ?: "Unknown Error Occurred"))
                }
            }
        }.flowOn(ioDispatcher)
    }
}
