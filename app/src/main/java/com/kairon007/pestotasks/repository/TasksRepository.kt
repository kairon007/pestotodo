package com.kairon007.pestotasks.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.kairon007.pestotasks.models.TaskModel
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TasksRepository @Inject constructor(
    private val firebaseDatabase: DatabaseReference
) {
    fun addTask(task: TaskModel) {
        val newTaskRef = firebaseDatabase.push()
        val taskId = newTaskRef.key
        val newTask = TaskModel(
            taskId ?: "", // Ensure taskId is not null
            title = task.title,
            description = task.description,
            status = task.status
        )

        newTaskRef.setValue(newTask)
    }
    fun updateTask(taskId: String, updatedTitle: String, updatedDescription: String,status:String) {
        val taskUpdates = hashMapOf<String, Any>(
            "title" to updatedTitle,
            "description" to updatedDescription,
            "status" to status
        )
        firebaseDatabase.child(taskId).updateChildren(taskUpdates)
    }
    fun deleteTask(taskId: String) {
        firebaseDatabase.child(taskId).removeValue()
    }
    fun getTasks(callback: (List<TaskModel>) -> Unit) {
        firebaseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tasks = mutableListOf<TaskModel>()
                for (data in snapshot.children) {
                    val task = data.getValue(TaskModel::class.java)
                    task?.let { tasks.add(it) }
                }
                callback(tasks)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

}
