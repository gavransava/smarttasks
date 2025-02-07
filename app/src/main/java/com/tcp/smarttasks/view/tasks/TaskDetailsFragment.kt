package com.tcp.smarttasks.view.tasks

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.tcp.smarttasks.data.domain.Task
import com.tcp.smarttasks.databinding.FragmentTaskDetailsBinding
import com.tcp.smarttasks.util.DateUtil
import com.tcp.smarttasks.view.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskDetailsFragment : Fragment() {

    private lateinit var binding: FragmentTaskDetailsBinding
    private val viewModel: TasksViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskId = arguments?.getString("taskId")

        binding.task.tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                if (taskId != null) {
                    val task = viewModel.getTask(taskId)
                    initViews(task)
                }
            }
        }
    }

    private fun initViews(task: Task) {
        binding.task.tvTitle.text = task.title
        binding.task.tvDueDate.text = DateUtil.formatDueDate(task.dueDate)
        binding.task.tvDaysLeft.text = DateUtil.calculateDaysLeft(task.dueDate, requireContext())
        binding.description.text = task.description

        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}