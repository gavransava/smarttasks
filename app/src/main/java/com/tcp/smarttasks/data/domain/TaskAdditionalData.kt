package com.tcp.smarttasks.data.domain

import androidx.room.Embedded
import androidx.room.Relation
import com.tcp.smarttasks.data.entity.TaskAdditionalDataEntity
import com.tcp.smarttasks.data.entity.TaskEntity

data class TaskAdditionalData(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "task_id"
    )
    val additionalData: TaskAdditionalDataEntity?
)