package com.tcp.smarttasks.view.tasks

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tcp.smarttasks.R
import com.tcp.smarttasks.data.domain.Task
import com.tcp.smarttasks.data.domain.TaskStatus
import com.tcp.smarttasks.databinding.TaskItemBinding
import com.tcp.smarttasks.util.DateUtil

class TasksListAdapter(val context: Context, private val itemClickListener: (String) -> Unit) :
    RecyclerView.Adapter<TasksListAdapter.TaskViewHolder>() {

    private var taskList: List<Task> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding, context, itemClickListener)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun submitList(newTaskList: List<Task>) {
        val diffCallback = TaskDiffCallback(taskList, newTaskList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        taskList = newTaskList
        diffResult.dispatchUpdatesTo(this)
    }

    class TaskViewHolder(
        private val binding: TaskItemBinding,
        private val context: Context,
        private val itemClickListener: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.taskItemContents.tvTitle.text = task.title
            binding.taskItemContents.tvDueDate.text = DateUtil.formatDueDate(task.dueDate)
            binding.taskItemContents.tvDaysLeft.text = DateUtil.calculateDaysLeft(task.dueDate, context)

            when (task.status) {
                TaskStatus.UNRESOLVED -> {
                    binding.ivTaskStatus.visibility = VISIBLE
                    binding.ivTaskStatus.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.unresolved_sign,
                            context.theme
                        )
                    )
                }

                TaskStatus.RESOLVED -> {
                    binding.ivTaskStatus.visibility = VISIBLE
                    binding.ivTaskStatus.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.sign_resolved,
                            context.theme
                        )
                    )
                }

                null -> {
                    binding.ivTaskStatus.visibility = GONE
                }
            }

            binding.container.setOnClickListener {
                itemClickListener.invoke(task.id)
            }
        }
    }

    class TaskDiffCallback(
        private val oldList: List<Task>,
        private val newList: List<Task>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}