package com.tcp.smarttasks.repository

import com.tcp.smarttasks.data.TasksDao
import com.tcp.smarttasks.data.mapper.toDomain
import com.tcp.smarttasks.data.mapper.toEntity
import com.tcp.smarttasks.network.Resource
import com.tcp.smarttasks.network.TasksService
import com.tcp.smarttasks.network.model.NetworkTasks
import com.tcp.smarttasks.util.NetworkConnectivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DataRepositoryImpl @Inject constructor(
    private val networkConnectivity: NetworkConnectivity,
    private val tasksService: TasksService,
    private val ioDispatcher: CoroutineContext,
    private val tasksDao: TasksDao
) : DataRepository {
    override fun fetchTasks(): Flow<Resource<NetworkTasks>> {
        return flow {
            if (!networkConnectivity.isConnected()) {
                emit(Resource.Error("No Internet Connection"))
            } else {
                try {
                    val response = tasksService.fetchTasks()
                    if (response.isSuccessful) {
                        response.body()?.let { networkTasks ->
                            val tasks = networkTasks.tasks.filter {
                                it.targetDate != null && it.title != null && it.description != null && it.priority != null
                            }.map { it.toDomain() }

                            tasksDao.saveTasks(tasks.map { it.toEntity() })
                            emit(Resource.Success(networkTasks))
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
