package com.tcp.smarttasks.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class NetworkTask(
    @Json(name = "id") val id: String,
    @Json(name = "TargetDate") val targetDate: String?,
    @Json(name = "DueDate") val dueDate: String?,
    @Json(name = "Title") val title: String?,
    @Json(name = "Description") val description: String?,
    @Json(name = "Priority") val priority: Int?
)