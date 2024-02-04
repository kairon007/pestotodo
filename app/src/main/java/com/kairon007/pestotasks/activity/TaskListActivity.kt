package com.kairon007.pestotasks.activity

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.kairon007.pestotasks.R
import com.kairon007.pestotasks.databinding.ActivityTaskListBinding
import com.kairon007.pestotasks.models.TaskModel
import com.kairon007.pestotasks.ui.TaskAdapter
import com.kairon007.pestotasks.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListActivity : AppCompatActivity() {
    private var taskAdapter: TaskAdapter? = null
    private lateinit var binding: ActivityTaskListBinding
    private val taskViewModel: TaskViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.tasks_list)
        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        binding.fabAddTask.setOnClickListener {
            showBottomSheetForTask()
        }
        taskViewModel.initialise()
        taskViewModel.isUserSignedIn.observe(this){
           if(!it){
               val intent = Intent(this@TaskListActivity, LoginActivity::class.java)
               startActivity(intent)
               finish()
           }
        }
        taskViewModel.tasks.observe(this) { tasks ->
            taskAdapter = TaskAdapter(this@TaskListActivity, tasks) {
                showBottomSheetForTask(it)
            }
            binding.recyclerViewTasks.adapter = taskAdapter
        }

    }

   private fun showBottomSheetForTask(task: TaskModel? = null) {
       val bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_task, null)
       val dialog = BottomSheetDialog(this, R.style.TaskBottomSheetDialog)
       dialog.setContentView(bottomSheetView)
       dialog.behavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels / 2

       // Set up views in the bottom sheet
       val editTitle = bottomSheetView.findViewById<TextInputEditText>(R.id.editTitle)
       val editDescription = bottomSheetView.findViewById<TextInputEditText>(R.id.editDescription)
       val btnSave = bottomSheetView.findViewById<Button>(R.id.btnSave)
       val btnDelete = bottomSheetView.findViewById<Button>(R.id.btnDelete)
       val statusSpinner = bottomSheetView.findViewById<Spinner>(R.id.spinnerStatus)

       // Set initial data for editing task
       task?.let {
           editTitle.setText(it.title)
           editDescription.setText(it.description)

           val statusAdapter = ArrayAdapter.createFromResource(
               this,
               R.array.status_array,
               android.R.layout.simple_spinner_item
           )
           statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
           statusSpinner.adapter = statusAdapter

           // Set the selected status in the spinner
           val statusPosition = statusAdapter.getPosition(it.status)
           statusSpinner.setSelection(statusPosition)

           // Show delete button for editing tasks
           btnDelete.visibility = View.VISIBLE
           btnDelete.setOnClickListener {
               task.taskId?.let { it1 -> taskViewModel.deleteTask(it1) }
               dialog.dismiss()
               Toast.makeText(this,
                   getString(R.string.task_deleted_successfully), Toast.LENGTH_SHORT).show()
           }
       }

       // Set up save button click listener
       btnSave.setOnClickListener {
           val title = editTitle.text.toString()
           val description = editDescription.text.toString()
           val status = statusSpinner.selectedItem.toString()

           if (task == null) {
               // Adding new task
               taskViewModel.createNewTask(title, description, status)
               Toast.makeText(this, getString(R.string.task_added_successfully), Toast.LENGTH_SHORT).show()
           } else {
               // Updating existing task
               task.taskId?.let { taskId ->
                   taskViewModel.updateTask(taskId, title, description, status)
                   Toast.makeText(this,
                       getString(R.string.task_updated_successfully), Toast.LENGTH_SHORT).show()
               }
           }

           // Dismiss the bottom sheet
           dialog.dismiss()
       }

       dialog.show()
   }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_logout -> {
                taskViewModel.logout()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_tasks, menu)
        return true
    }


}
