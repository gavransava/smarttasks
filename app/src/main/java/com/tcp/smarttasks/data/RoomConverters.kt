package com.tcp.smarttasks.data

import androidx.room.TypeConverter
import com.tcp.smarttasks.data.domain.TaskStatus

class RoomConverters {

    @TypeConverter
    fun fromResolveStatus(status: TaskStatus): String {
        return status.name
    }

    @TypeConverter
    fun toResolveStatus(status: String): TaskStatus {
        return TaskStatus.valueOf(status)
    }
}