package com.tcp.smarttasks.view.tasks

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tcp.smarttasks.R
import com.tcp.smarttasks.data.domain.Task
import com.tcp.smarttasks.databinding.TaskItemBinding
import com.tcp.smarttasks.util.DateUtil
import java.time.LocalDate

class TasksListAdapter(val context: Context) : RecyclerView.Adapter<TasksListAdapter.TaskViewHolder>() {

    private var taskList: List<Task> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding, context)
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

    class TaskViewHolder(private val binding: TaskItemBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.tvTitle.text = task.title
            binding.tvDueDate.text = task.dueDate ?: "N/A"

            try {
                if (task.dueDate != null) {
                    val daysLeft = DateUtil.calculateDaysDifference(DateUtil.formatLocalDate(LocalDate.now()), task.dueDate)
                    binding.tvDaysLeft.text = "$daysLeft"
                } else {
                    binding.tvDaysLeft.text = "N/A"
                }
            } catch (e: IllegalArgumentException) {
                binding.tvDaysLeft.text = context.getString(R.string.overdue)
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