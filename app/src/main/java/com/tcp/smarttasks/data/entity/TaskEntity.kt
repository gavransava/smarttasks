package com.tcp.smarttasks.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "target_date")
    val targetDate: String,
    @ColumnInfo(name = "due_date")
    val dueDate: String? = null,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "priority")
    val priority: Int
)