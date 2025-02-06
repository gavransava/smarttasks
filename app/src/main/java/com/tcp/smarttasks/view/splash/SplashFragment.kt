package com.tcp.smarttasks.view.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.tcp.smarttasks.databinding.FragmentSplashBinding
import com.tcp.smarttasks.util.showErrorDialog
import com.tcp.smarttasks.view.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private val viewModel: TasksViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.tasks.collect { uiState ->
                    if (uiState is TasksViewModel.TasksUiState.Success) {
                        // Delay here to appreciate the nice splash screen
                        delay(1000)
                    }
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
                findNavController().navigate(
                    SplashFragmentDirections.actionSplashFragmentToTasksFragment()
                )
            }

            is TasksViewModel.TasksUiState.Error -> {
                showErrorDialog(TAG, uiState.message)
            }
        }
    }

    companion object {
        const val TAG = "SplashFragment"
    }
}