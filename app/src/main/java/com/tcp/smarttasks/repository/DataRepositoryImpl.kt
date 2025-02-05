package com.tcp.smarttasks.repository

import com.tcp.smarttasks.network.Resource
import com.tcp.smarttasks.network.TasksService
import com.tcp.smarttasks.network.model.NetworkTasks
import com.tcp.smarttasks.util.NetworkConnectivityImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DataRepositoryImpl @Inject constructor(
    private val networkConnectivity: NetworkConnectivityImpl,
    private val tasksService: TasksService,
    private val ioDispatcher: CoroutineContext
) : DataRepository {
    override suspend fun requestTasks(): Flow<Resource<NetworkTasks>> {
        return flow {
            if (!networkConnectivity.isConnected()) {
                emit(Resource.Error("No Internet Connection"))
            } else {
                val response = tasksService.fetchTasks()
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(it))
                    } ?: emit(Resource.Error("Empty response body"))
                } else {
                    emit(Resource.Error("Error: ${response.message()}"))
                }
            }
        }.flowOn(ioDispatcher)
    }
}
