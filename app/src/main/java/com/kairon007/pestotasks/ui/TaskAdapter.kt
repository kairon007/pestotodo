package com.kairon007.pestotasks.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kairon007.pestotasks.R
import com.kairon007.pestotasks.models.TaskModel
import com.kairon007.pestotasks.ui.TaskAdapter.TaskViewHolder

class TaskAdapter(
    private val context: Context,
    private val taskList: List<TaskModel>,
    private val onItemClick: (TaskModel) -> Unit
) :
    RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtTitle: TextView
        private val txtDescription: TextView
        private val txtStatus: TextView

        init {
            txtTitle = itemView.findViewById(R.id.txtTitle)
            txtDescription = itemView.findViewById(R.id.txtDescription)
            txtStatus = itemView.findViewById(R.id.txtStatus)
        }

        fun bind(task: TaskModel) {
            txtTitle.text = task.title
            txtDescription.text = task.description
            txtStatus.text = task.status
            itemView.setOnClickListener {
                onItemClick.invoke(task)
            }
        }
    }
}
