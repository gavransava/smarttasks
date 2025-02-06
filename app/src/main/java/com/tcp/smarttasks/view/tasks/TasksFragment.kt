package com.tcp.smarttasks.view.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tcp.smarttasks.databinding.FragmentTasksBinding
import com.tcp.smarttasks.util.showErrorDialog
import com.tcp.smarttasks.view.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class TasksFragment : Fragment() {

    private lateinit var binding: FragmentTasksBinding
    private val viewModel: TasksViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.tasks.collect { uiState ->
                    handleUiState(uiState)
                }
            }
        }
    }

    private fun handleUiState(uiState: TasksViewModel.TasksUiState) {
        when (uiState) {
            TasksViewModel.TasksUiState.InitialState -> {}
            TasksViewModel.TasksUiState.Loading -> {
                // show some loader (not in UI spec)
            }

            is TasksViewModel.TasksUiState.Success -> {
                // feed adapter
                Timber.d("feed adapter")
            }

            is TasksViewModel.TasksUiState.Error -> {
                showErrorDialog(TAG, uiState.message)
            }
        }
    }

    companion object {
        const val TAG = "TasksFragment"
    }
}