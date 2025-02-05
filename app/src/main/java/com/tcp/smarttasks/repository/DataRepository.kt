package com.tcp.smarttasks.repository

import com.tcp.smarttasks.network.Resource
import com.tcp.smarttasks.network.model.NetworkTasks
import kotlinx.coroutines.flow.Flow

interface DataRepository {
    suspend fun requestTasks(): Flow<Resource<NetworkTasks>>
}