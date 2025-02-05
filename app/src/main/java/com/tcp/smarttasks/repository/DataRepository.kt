package com.tcp.smarttasks.repository

import com.tcp.smarttasks.network.Resource
import com.tcp.smarttasks.network.model.NetworkTasks
import kotlinx.coroutines.flow.Flow

interface DataRepository {
    fun fetchTasks(): Flow<Resource<NetworkTasks>>
}