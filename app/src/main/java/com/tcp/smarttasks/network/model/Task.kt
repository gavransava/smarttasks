package com.tcp.smarttasks.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    @SerialName("id") val id: String,
    @SerialName("TargetDate") val targetDate: String,
    @SerialName("DueDate") val dueDate: String,
    @SerialName("Title") val title: String,
    @SerialName("Description") val description: String,
    @SerialName("Priority") val priority: Int
)