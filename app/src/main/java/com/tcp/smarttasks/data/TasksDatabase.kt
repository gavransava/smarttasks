package com.tcp.smarttasks.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tcp.smarttasks.data.entity.TaskAdditionalDataEntity
import com.tcp.smarttasks.data.entity.TaskEntity


@Database(
    entities = [TaskEntity::class, TaskAdditionalDataEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(RoomConverters::class)
abstract class TasksDatabase : RoomDatabase() {

    abstract fun tasksDao(): TasksDao
}