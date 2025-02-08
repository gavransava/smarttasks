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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tcp.smarttasks.R
import com.tcp.smarttasks.data.domain.Task
import com.tcp.smarttasks.databinding.FragmentTasksBinding
import com.tcp.smarttasks.util.DateUtil
import com.tcp.smarttasks.util.showErrorDialog
import com.tcp.smarttasks.view.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class TasksFragment : Fragment() {

    private lateinit var binding: FragmentTasksBinding
    private val viewModel: TasksViewModel by activityViewModels()
    private lateinit var tasksListAdapter: TasksListAdapter
    private var selectedDate = LocalDate.now()
    private var tasksData = mutableListOf<Task>()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        showErrorDialog(exception.localizedMessage)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setOnClickListeners()

        viewLifecycleOwner.lifecycleScope.launch(exceptionHandler) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tasks.collect { uiState ->
                    handleUiState(uiState)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        tasksListAdapter = TasksListAdapter { taskId ->
            taskSelected(taskId)
        }

        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tasksListAdapter
        }
    }

    private fun taskSelected(taskId: String) {
        findNavController().navigate(
            TasksFragmentDirections.actionTasksFragmentToTaskDetailsFragment(taskId)
        )
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
        binding.header.titleDateLabel.text =
            if (selectedDate == LocalDate.now()) getString(R.string.today) else DateUtil.localDateUIFormat(
                selectedDate
            )
        val tasksForSelectedDate = filterTasksForSelectedDate(tasksData)
        tasksListAdapter.submitList(tasksForSelectedDate)
        binding.rvTasks.scrollToPosition(0)
        updateNoTaskView(tasksForSelectedDate)
    }

    private fun updateNoTaskView(tasksForSelectedDate: List<Task>) {
        val visibility = if (tasksForSelectedDate.isEmpty()) VISIBLE else GONE
        binding.ivNoTasks.visibility = visibility
        binding.tvNoTasks.visibility = visibility
    }

    private fun handleUiState(uiState: TasksViewModel.TasksUiState) {
        when (uiState) {
            TasksViewModel.TasksUiState.InitialState -> {}
            TasksViewModel.TasksUiState.Loading -> {
                // show some loader (not in UI spec)
            }

            is TasksViewModel.TasksUiState.Success -> {
                tasksData = uiState.data.toMutableList()
                updateAdapter()
            }

            is TasksViewModel.TasksUiState.Error -> {
                showErrorDialog(uiState.message)
            }
        }
    }

    private fun filterTasksForSelectedDate(data: List<Task>): List<Task> {
        return data.filter {
            DateUtil.formatLocalDate(selectedDate) == it.targetDate
        }
    }
}