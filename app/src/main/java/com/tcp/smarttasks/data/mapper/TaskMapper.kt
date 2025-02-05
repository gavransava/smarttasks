package com.tcp.smarttasks.data.mapper

import com.tcp.smarttasks.data.domain.Task
import com.tcp.smarttasks.data.entity.TaskEntity
import com.tcp.smarttasks.network.model.NetworkTask

fun NetworkTask.toDomain(): Task {
    return Task(
        id = this.id,
        targetDate = this.targetDate ?: "",
        dueDate = this.dueDate,
        title = this.title ?: "",
        description = this.description ?: "",
        priority = this.priority ?: 0
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
        priority = this.priority
    )
}