package com.tcp.smarttasks.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkTasks(
    @Json(name = "tasks") val tasks: List<NetworkTask>
)