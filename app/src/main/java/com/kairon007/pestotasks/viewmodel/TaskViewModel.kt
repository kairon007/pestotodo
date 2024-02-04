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
    private val _filteredTasks = MutableLiveData<List<TaskModel>>()
    val filteredTasks: LiveData<List<TaskModel>> get() = _filteredTasks

    private val _isUserSignedIn = MutableLiveData<Boolean>()
    val isUserSignedIn: LiveData<Boolean> get() = _isUserSignedIn
    private var currentFilter: String = "All"

    fun logout() {
        authRepository.logout()
    }
    fun deleteTask(taskId: String) {
        tasksRepository.deleteTask(taskId)
    }
    private fun filterTasks(allTasks: List<TaskModel>): List<TaskModel> {
        return when (currentFilter) {
            "All" -> allTasks // Return all tasks
            "To Do" -> allTasks.filter { it.status == "To Do" }
            "In Progress" -> allTasks.filter { it.status == "In Progress" }
            "Done" -> allTasks.filter { it.status == "Done" }
            else -> allTasks // Default to all tasks
        }
    }
    fun setFilter(filter: String) {
        currentFilter = filter
        loadTasks()
    }

    private fun checkUserAuthentication() {
       viewModelScope.launch {  _isUserSignedIn.value = authRepository.getCurrentUser() }
    }
    fun initialise(){
        checkUserAuthentication()
        loadTasks()
    }

    fun createNewTask(title: String, description: String, status: String) {
        viewModelScope.launch {
            val newTask = TaskModel(taskId = "", title = title, description = description, status = status)
            tasksRepository.addTask(newTask)
        }
    }
    fun loadTasks() {
        tasksRepository.getTasks { tasks ->
            val filteredTasks = filterTasks(tasks)
            _filteredTasks.postValue(filteredTasks)
        }
    }


    fun updateTask(taskId: String, updatedTitle: String, updatedDescription: String,status:String) {
        tasksRepository.updateTask(taskId, updatedTitle, updatedDescription,status)
    }
}
