package com.tcp.smarttasks.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcp.smarttasks.data.domain.Task
import com.tcp.smarttasks.network.Resource
import com.tcp.smarttasks.repository.DataRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val dataRepositoryImpl: DataRepositoryImpl
) : ViewModel() {

    private val _tasks: MutableStateFlow<TasksUiState> = MutableStateFlow(TasksUiState.InitialState)
    val tasks: StateFlow<TasksUiState> = _tasks

    init {
        collectTasks()
    }

    /**
     * Starts collecting tasks from Room database and refreshing the db (in onStart) with data
     * from remote by calling fetchTasks
     */
    private fun collectTasks() {
        viewModelScope.launch {
            dataRepositoryImpl.getAllTasks().filterNotNull().onStart {
                fetchTasks()
            }.distinctUntilChanged().collect { taskList ->
                _tasks.update { TasksUiState.Success(taskList) }
            }
        }
    }

    /**
     * Call to remote to fetch tasks; upon successful response, new tasks are written to db
     */
    private fun fetchTasks() {
        viewModelScope.launch {
            dataRepositoryImpl.fetchTasks().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _tasks.update { TasksUiState.Loading }
                    }

                    is Resource.Success -> {
                        // We get data from db so just log success
                        Timber.d("Tasks fetched")
                    }

                    is Resource.Error -> {
                        Timber.d(resource.message)
                        _tasks.update { TasksUiState.Error(resource.message) }
                    }
                }
            }
        }
    }

    sealed class TasksUiState {
        data object InitialState : TasksUiState()
        data object Loading : TasksUiState()
        data class Success(val data: List<Task>) : TasksUiState()
        data class Error(val message: String) : TasksUiState()
    }
}