package com.kairon007.pestotasks.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kairon007.pestotasks.repository.AuthRepository
import com.kairon007.pestotasks.repository.TasksRepository
import com.kairon007.pestotasks.viewmodel.TaskViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argThat

@ExperimentalCoroutinesApi
class TaskViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testDispatcher)

    @Mock
    lateinit var authRepository: AuthRepository

    @Mock
    lateinit var tasksRepository: TasksRepository



    private lateinit var taskViewModel: TaskViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        Mockito.reset(authRepository)
        runBlocking {
            taskViewModel = TaskViewModel(authRepository, tasksRepository)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testCoroutineScope.cleanupTestCoroutines()
    }
    @Test
    fun `initialise sets isUserSignedIn to true when user is signed in`() {
        // Given
        `when`(authRepository.getCurrentUser()).thenReturn(true)

        // When
        taskViewModel.initialise()

        // Then
        assert(taskViewModel.isUserSignedIn.value == true)
    }
    @Test
    fun `initialise sets isUserSignedIn to false when user is not signed in`() {
        // Given
        `when`(authRepository.getCurrentUser()).thenReturn(false)

        // When
        taskViewModel.initialise()

        // Then
        assert(taskViewModel.isUserSignedIn.value == false)
    }


    @Test
    fun `check user authentication calls repository`() = testCoroutineScope.runBlockingTest {
        // Given
        `when`(authRepository.getCurrentUser()).thenReturn(true)

        // When
        taskViewModel = TaskViewModel(authRepository, tasksRepository)
        taskViewModel.initialise()

        // Then
        // Use runBlockingTest and advanceUntilIdle to wait for coroutines to complete
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that getCurrentUser is called exactly once
        verify(authRepository, times(1)).getCurrentUser()
    }
    @Test
    fun `create new task calls repository`() = testCoroutineScope.runBlockingTest {
        // Given
        val title = "Task Title"
        val description = "Task Description"
        val status = "To Do"

        // When
        taskViewModel.createNewTask(title, description, status)

        // Then
        // Use runBlockingTest and advanceUntilIdle to wait for coroutines to complete
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that addTask is called with the expected TaskModel
        verify(tasksRepository).addTask(argThat {
            this.title == title && this.description == description && this.status == status
        })
    }

    @Test
    fun `update task calls repository`() {
        // Given
        val taskId = "task123"
        val updatedTitle = "Updated Task Title"
        val updatedDescription = "Updated Task Description"
        val updatedStatus = "Done"

        // When
        taskViewModel.updateTask(taskId, updatedTitle, updatedDescription, updatedStatus)

        // Then
        verify(tasksRepository).updateTask(taskId, updatedTitle, updatedDescription, updatedStatus)
    }
}
