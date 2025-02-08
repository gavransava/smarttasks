package com.tcp.smarttasks.view.tasks

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
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
import com.tcp.smarttasks.util.showAddCommentDialog
import com.tcp.smarttasks.util.showErrorDialog
import com.tcp.smarttasks.view.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskDetailsFragment : Fragment() {

    private lateinit var binding: FragmentTaskDetailsBinding
    private val viewModel: TasksViewModel by activityViewModels()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        showErrorDialog(exception.localizedMessage)
    }

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
        setupBackButton()

        viewLifecycleOwner.lifecycleScope.launch(exceptionHandler) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.taskDetails.collect { task ->
                    task?.let {
                        initViews(task)
                    }
                }
            }
        }
    }

    private fun setupBackButton() {
        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initViews(task: Task) {
        updateTaskDetails(task)
        updateUserComment(task)
        updateStatusButtons(task)
        setupStatusButtonActions(task)
    }

    private fun updateTaskDetails(task: Task) {
        binding.task.tvTitle.text = task.title
        binding.task.tvDueDate.text = DateUtil.formatDueDate(task.dueDate)
        binding.task.tvDaysLeft.text = DateUtil.calculateDaysLeft(task.dueDate, requireContext())
        binding.description.text = task.description
    }

    private fun updateUserComment(task: Task) {
        if (!task.userComment.isNullOrEmpty()) {
            setViewVisibility(binding.userCommentLabel, binding.userComment, binding.separator3, visibility = VISIBLE)
            binding.userComment.text = task.userComment
        } else {
            setViewVisibility(binding.userCommentLabel, binding.userComment, binding.separator3,visibility = GONE)
        }
    }

    private fun setViewVisibility(vararg views: View, visibility: Int) {
        views.forEach { it.visibility = visibility }
    }

    private fun updateStatusButtons(task: Task) {
        when (task.status) {
            TaskStatus.UNRESOLVED -> {
                setTextColorBasedOnStatus(resources.getColor(R.color.st_red, requireContext().theme))
                setTaskStatusImage(R.drawable.unresolved_sign)
                hideStatusButtons()
            }
            TaskStatus.RESOLVED -> {
                binding.taskStatus.text = getText(R.string.resolved)
                setTextColorBasedOnStatus(resources.getColor(R.color.st_green, requireContext().theme))
                setTaskStatusImage(R.drawable.sign_resolved)
                hideStatusButtons()
            }
            null -> {
                showStatusButtons()
            }
        }
    }

    private fun setupStatusButtonActions(task: Task) {
        if (binding.btnResolve.isVisible && binding.btnCantResolve.isVisible) {
            setStatusButtonClickListener(binding.btnResolve, TaskStatus.RESOLVED, task)
            setStatusButtonClickListener(binding.btnCantResolve, TaskStatus.UNRESOLVED, task)
        }
    }

    private fun setStatusButtonClickListener(button: View, status: TaskStatus, task: Task) {
        button.setOnClickListener {
            showAddCommentDialog { comment ->
                updateTaskStatus(task.id, status, comment)
            }
        }
    }

    private fun updateTaskStatus(taskId: String, status: TaskStatus, comment: String) {
        viewLifecycleOwner.lifecycleScope.launch(exceptionHandler) {
            viewModel.setTaskStatus(taskId = taskId, status = status, comment = comment)
        }
    }

    private fun setTextColorBasedOnStatus(color: Int) {
        binding.task.tvTitle.setTextColor(color)
        binding.task.tvDueDate.setTextColor(color)
        binding.task.tvDaysLeft.setTextColor(color)
        binding.taskStatus.setTextColor(color)
    }

    private fun setTaskStatusImage(drawableRes: Int) {
        binding.ivTaskStatus.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                drawableRes,
                requireContext().theme
            )
        )
    }

    private fun showStatusButtons() {
        binding.btnResolve.visibility = VISIBLE
        binding.btnCantResolve.visibility = VISIBLE
    }

    private fun hideStatusButtons() {
        binding.btnResolve.visibility = GONE
        binding.btnCantResolve.visibility = GONE
    }
}