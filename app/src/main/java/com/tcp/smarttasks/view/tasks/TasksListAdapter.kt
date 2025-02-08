package com.tcp.smarttasks.view.tasks

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tcp.smarttasks.R
import com.tcp.smarttasks.data.domain.Task
import com.tcp.smarttasks.data.domain.TaskStatus
import com.tcp.smarttasks.databinding.TaskItemBinding
import com.tcp.smarttasks.util.DateUtil

class TasksListAdapter(private val itemClickListener: (String) -> Unit) :
    ListAdapter<Task, TasksListAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    class TaskViewHolder(
        private val binding: TaskItemBinding,
        private val itemClickListener: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.taskItemContents.tvTitle.text = task.title
            binding.taskItemContents.tvDueDate.text = DateUtil.formatDueDate(task.dueDate)
            binding.taskItemContents.tvDaysLeft.text = DateUtil.calculateDaysLeft(task.dueDate, binding.root.context)

            setStatusImage(task.status)

            binding.container.setOnClickListener {
                itemClickListener.invoke(task.id)
            }
        }

        private fun setStatusImage(status: TaskStatus?) {
            binding.ivTaskStatus.visibility = if (status != null) VISIBLE else GONE
            when (status) {
                TaskStatus.UNRESOLVED -> {
                    binding.ivTaskStatus.setImageResource(R.drawable.unresolved_sign)
                }

                TaskStatus.RESOLVED -> {
                    binding.ivTaskStatus.setImageResource(R.drawable.sign_resolved)
                }

                null -> {
                    // No image for default unresolved state
                }
            }
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}