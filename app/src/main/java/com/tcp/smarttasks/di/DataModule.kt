package com.tcp.smarttasks.di

import com.tcp.smarttasks.repository.DataRepository
import com.tcp.smarttasks.repository.DataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun provideDataRepository(dataRepositoryImpl: DataRepositoryImpl): DataRepository
}
