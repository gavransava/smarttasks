package com.tcp.smarttasks.view

import androidx.lifecycle.ViewModel
import com.tcp.smarttasks.network.Resource
import com.tcp.smarttasks.network.model.NetworkTasks
import com.tcp.smarttasks.repository.DataRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val dataRepositoryImpl: DataRepositoryImpl
) : ViewModel() {

    private val _tasks: MutableStateFlow<Resource<NetworkTasks>> =
        MutableStateFlow(Resource.Loading)
    val tasks: StateFlow<Resource<NetworkTasks>> = _tasks

    fun fetchTasks(): Flow<Resource<NetworkTasks>> {
        return dataRepositoryImpl.fetchTasks()
    }
}