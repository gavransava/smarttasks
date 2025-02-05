package com.tcp.smarttasks.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkTasks(
    @SerialName("tasks") val tasks: List<Task>
)