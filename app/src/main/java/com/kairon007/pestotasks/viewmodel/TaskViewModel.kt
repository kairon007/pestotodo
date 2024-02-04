package com.kairon007.pestotasks.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kairon007.pestotasks.models.TaskModel
import com.kairon007.pestotasks.repository.AuthRepository
import com.kairon007.pestotasks.repository.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tasksRepository: TasksRepository
) : ViewModel() {

    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> get() = _tasks
    private val _isUserSignedIn = MutableLiveData<Boolean>()
    val isUserSignedIn: LiveData<Boolean> get() = _isUserSignedIn

    fun deleteTask(taskId: String) {
        tasksRepository.deleteTask(taskId)
    }

    private fun checkUserAuthentication() {
       viewModelScope.launch {  _isUserSignedIn.value = authRepository.getCurrentUser() }
    }
    fun initialise(){
        checkUserAuthentication()
        loadTasks()
    }

    fun createNewTask(title: String, description: String, status: String) {
        // Example: Use viewModelScope to launch a coroutine for background work
        viewModelScope.launch {
            // Example: Create a new task and add it to the repository
            val newTask = TaskModel(taskId = "", title = title, description = description, status = status)
            tasksRepository.addTask(newTask)
        }
    }
    private fun loadTasks() {
        // Example: Use the repository to get tasks from Firebase
        tasksRepository.getTasks { tasks ->
            _tasks.postValue(tasks)
        }
    }


    fun updateTask(taskId: String, updatedTitle: String, updatedDescription: String,status:String) {
        tasksRepository.updateTask(taskId, updatedTitle, updatedDescription,status)
    }
}
