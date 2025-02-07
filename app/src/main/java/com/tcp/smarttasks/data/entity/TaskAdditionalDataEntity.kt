package com.tcp.smarttasks.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.tcp.smarttasks.data.domain.TaskStatus

@Entity(
    tableName = "task_additional_data",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["task_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskAdditionalDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "task_id")
    val taskId: String, // Foreign key to TaskEntity
    @ColumnInfo(name = "task_status")
    val taskStatus: TaskStatus? = null,
    @ColumnInfo(name = "user_comment")
    val userComment: String? = null
)