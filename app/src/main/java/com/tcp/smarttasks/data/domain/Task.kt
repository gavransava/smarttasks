package com.tcp.smarttasks.data.domain

data class Task(
    val id: String,
    val targetDate: String,
    val dueDate: String?,
    val title: String,
    val description: String,
    val priority: Int,
    val status: TaskStatus?,
    val userComment: String?
)