package com.tcp.smarttasks.data.mapper

import com.tcp.smarttasks.data.domain.Task
import com.tcp.smarttasks.data.domain.TaskAdditionalData
import com.tcp.smarttasks.data.entity.TaskEntity
import com.tcp.smarttasks.network.model.NetworkTask

fun NetworkTask.toDomain(): Task {
    return Task(
        id = this.id,
        targetDate = this.targetDate ?: "",
        dueDate = this.dueDate,
        title = this.title ?: "",
        description = this.description ?: "",
        priority = this.priority ?: 0,
        status = null,
        userComment = null
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        targetDate = this.targetDate,
        dueDate = this.dueDate,
        title = this.title,
        description = this.description,
        priority = this.priority
    )
}

fun TaskEntity.toDomain(): Task {
    return Task(
        id = this.id,
        targetDate = this.targetDate,
        dueDate = this.dueDate,
        title = this.title,
        description = this.description,
        priority = this.priority,
        status = null,
        userComment = null
    )
}

fun TaskAdditionalData.toDomain(): Task {
    return Task(
        id = this.task.id,
        targetDate = this.task.targetDate,
        dueDate = this.task.dueDate,
        title = this.task.title,
        description = this.task.description,
        priority = this.task.priority,
        status = this.additionalData?.taskStatus,
        userComment = this.additionalData?.userComment
    )
}