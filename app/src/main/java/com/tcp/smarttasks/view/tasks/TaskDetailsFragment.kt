package com.tcp.smarttasks.view.tasks

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.tcp.smarttasks.R
import com.tcp.smarttasks.data.domain.Task
import com.tcp.smarttasks.data.domain.TaskStatus
import com.tcp.smarttasks.databinding.FragmentTaskDetailsBinding
import com.tcp.smarttasks.util.DateUtil
import com.tcp.smarttasks.view.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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
        taskId?.let {
            viewModel.getTask(it)
        }
        binding.task.tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.taskDetails.collect { task ->
                    task?.let {
                        initViews(task)
                    }
                }
            }
        }
    }

    private fun initViews(task: Task) {
        binding.task.tvTitle.text = task.title
        binding.task.tvDueDate.text = DateUtil.formatDueDate(task.dueDate)
        binding.task.tvDaysLeft.text = DateUtil.calculateDaysLeft(task.dueDate, requireContext())
        binding.description.text = task.description

        when(task.status){
            TaskStatus.UNRESOLVED -> {
                setTextColorBasedOnStatus(resources.getColor(R.color.st_red, requireContext().theme))
                binding.ivTaskStatus.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.unresolved_sign, requireContext().theme))
                hideStatusButtons()
            }
            TaskStatus.RESOLVED -> {
                binding.taskStatus.text = getText(R.string.resolved)
                setTextColorBasedOnStatus(resources.getColor(R.color.st_green, requireContext().theme))
                binding.ivTaskStatus.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.sign_resolved, requireContext().theme))
                hideStatusButtons()
            }
            null -> {}
        }

        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnResolve.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.setTaskStatus(taskId = task.id, status = TaskStatus.RESOLVED)
            }
        }

        binding.btnCantResolve.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.setTaskStatus(taskId = task.id, status = TaskStatus.UNRESOLVED)
            }
        }
    }

    private fun setTextColorBasedOnStatus(color: Int) {
        binding.task.tvTitle.setTextColor(color)
        binding.task.tvDueDate.setTextColor(color)
        binding.task.tvDaysLeft.setTextColor(color)
        binding.taskStatus.setTextColor(color)
    }

    private fun hideStatusButtons() {
        binding.btnResolve.visibility = GONE
        binding.btnCantResolve.visibility = GONE
    }
}