package com.tcp.smarttasks.network

import com.tcp.smarttasks.network.model.NetworkTasks
import retrofit2.Response
import retrofit2.http.GET

interface TasksService {
    @GET("/")
    suspend fun fetchTasks(
    ): Response<NetworkTasks>
}