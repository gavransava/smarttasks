package com.tcp.smarttasks.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tcp.smarttasks.data.entity.TaskEntity


@Database(
    entities = [(TaskEntity::class)],
    version = 1,
    exportSchema = false
)

abstract class TasksDatabase : RoomDatabase() {

    abstract fun tasksDao(): TasksDao
}