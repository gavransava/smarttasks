package com.tcp.smarttasks.di

import android.content.Context
import androidx.room.Room
import com.tcp.smarttasks.data.TasksDao
import com.tcp.smarttasks.data.TasksDatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(
            @ApplicationContext context: Context
        ): TasksDatabase {
            val builder = Room.databaseBuilder(
                context.applicationContext,
                TasksDatabase::class.java,
                "tasks_database"
            ).fallbackToDestructiveMigration()

            return builder.build()
        }

        @Provides
        fun provideTasksDao(
            taskDatabase: TasksDatabase
        ): TasksDao = taskDatabase.tasksDao()

    }
}

