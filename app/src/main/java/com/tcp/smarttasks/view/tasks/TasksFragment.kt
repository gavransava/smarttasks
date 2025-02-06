package com.tcp.smarttasks.view.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tcp.smarttasks.data.domain.Task
import com.tcp.smarttasks.databinding.FragmentTasksBinding
import com.tcp.smarttasks.util.DateUtil
import com.tcp.smarttasks.util.showErrorDialog
import com.tcp.smarttasks.view.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate

@AndroidEntryPoint
class TasksFragment : Fragment() {

    private lateinit var binding: FragmentTasksBinding
    private val viewModel: TasksViewModel by activityViewModels()
    private val tasksListAdapter = TasksListAdapter()
    private var selectedDate = LocalDate.now()
    private var tasksData = mutableListOf<Task>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.rvTasks
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = tasksListAdapter

        setOnClickListeners()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.tasks.collect { uiState ->
                    handleUiState(uiState)
                }
            }
        }
    }

    private fun setOnClickListeners() {
        binding.header.ibLeftArrow.setOnClickListener {
            selectedDate = selectedDate.minusDays(1)
            updateAdapter()
        }

        binding.header.ibRightArrow.setOnClickListener {
            selectedDate = selectedDate.plusDays(1)
            updateAdapter()
        }
    }

    private fun updateAdapter() {
        binding.header.titleDateLabel.text = DateUtil.formatLocalDate(selectedDate)
        val tasksForSelectedDate = filterTasksForSelectedDate(tasksData)
        tasksListAdapter.submitList(tasksForSelectedDate)
        if(tasksForSelectedDate.isEmpty()){
            binding.ivNoTasks.visibility = VISIBLE
            binding.tvNoTasks.visibility = VISIBLE
        } else {
            binding.ivNoTasks.visibility = GONE
            binding.tvNoTasks.visibility = GONE
        }
    }

    private fun handleUiState(uiState: TasksViewModel.TasksUiState) {
        when (uiState) {
            TasksViewModel.TasksUiState.InitialState -> {}
            TasksViewModel.TasksUiState.Loading -> {
                // show some loader (not in UI spec)
            }

            is TasksViewModel.TasksUiState.Success -> {
                tasksData = uiState.data.toMutableList()

                val tasksForSelectedDate = filterTasksForSelectedDate(uiState.data)
                tasksListAdapter.submitList(tasksForSelectedDate)
            }

            is TasksViewModel.TasksUiState.Error -> {
                showErrorDialog(TAG, uiState.message)
            }
        }
    }

    private fun filterTasksForSelectedDate(data: List<Task>): List<Task> {
        return data.filter {
            try {
                DateUtil.isDateBetween(
                    DateUtil.formatLocalDate(selectedDate), it.targetDate, it.dueDate
                )
            } catch (e: IllegalArgumentException) {
                Timber.d(e.message)
                false
            }
        }
    }

    companion object {
        const val TAG = "TasksFragment"
    }
}