package com.kairon007.pestotasks.repository

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.kairon007.pestotasks.models.TaskModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock

class TasksRepositoryTest {

    @Mock
    private lateinit var firebaseDatabase: DatabaseReference

    @Mock
    private lateinit var dataSnapshot: DataSnapshot

    @Mock
    private lateinit var callback: (List<TaskModel>) -> Unit

    private lateinit var tasksRepository: TasksRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        tasksRepository = TasksRepository(firebaseDatabase)
    }


    @Test
    fun `updateTask should update a task in the database`() {
        // Given
        val taskId = "123"
        val updatedTitle = "Updated Title"
        val updatedDescription = "Updated Description"
        val updatedStatus = "Done"
        val childReference = mock(DatabaseReference::class.java)

        `when`(firebaseDatabase.child(taskId)).thenReturn(childReference)

        // When
        tasksRepository.updateTask(taskId, updatedTitle, updatedDescription, updatedStatus)

        // Then
        val taskUpdates = hashMapOf<String, Any>(
            "title" to updatedTitle,
            "description" to updatedDescription,
            "status" to updatedStatus
        )
        verify(childReference).updateChildren(taskUpdates)
    }

    @Test
    fun `deleteTask should remove the task from the database`() {
        // Arrange
        val tasksRepository = TasksRepository(firebaseDatabase)
        val taskId = "1"

        // Mock DatabaseReference
        val databaseReference = mock(DatabaseReference::class.java)
        val childReference = mock(DatabaseReference::class.java)

        `when`(databaseReference.child(taskId)).thenReturn(childReference)
        `when`(childReference.removeValue()).thenAnswer {
            // Simulate removeValue() behavior
            null
        }

        // Mock firebaseDatabase
        `when`(firebaseDatabase.child(taskId)).thenReturn(childReference)

        // Act
        tasksRepository.deleteTask(taskId)

        // Assert
        // Add assertions as needed
        verify(childReference).removeValue()
    }

    @Test
    fun `getTasks should retrieve tasks from the database`() {
        // Arrange
        val tasksRepository = TasksRepository(firebaseDatabase)
        val tasks = listOf(TaskModel("1", "Task 1", "Description 1", "To Do"))

        // Mock dataSnapshot
        val dataSnapshot = mockDataSnapshot(tasks)

        // Mock DatabaseReference
        val databaseReference = mock(DatabaseReference::class.java)
        `when`(databaseReference.addValueEventListener(any())).thenAnswer {
            val eventListener = it.arguments[0] as ValueEventListener
            // Simulate onDataChange event
            eventListener.onDataChange(dataSnapshot)
            null // Return Unit
        }

        // Mock firebaseDatabase
        `when`(firebaseDatabase.addValueEventListener(any())).thenAnswer {
            val eventListener = it.arguments[0] as ValueEventListener
            // Simulate onDataChange event
            eventListener.onDataChange(dataSnapshot)
            null // Return Unit
        }

        // Act
        tasksRepository.getTasks { retrievedTasks ->
            // Assert
            assertEquals(tasks, retrievedTasks)
        }
    }

    private fun mockDataSnapshot(tasks: List<TaskModel>): DataSnapshot {
        val dataSnapshot = mock(DataSnapshot::class.java)
        val childrenList: MutableList<DataSnapshot> = mutableListOf()

        tasks.forEach { task ->
            val taskSnapshot = mock(DataSnapshot::class.java)
            `when`(taskSnapshot.getValue(TaskModel::class.java)).thenReturn(task)
            childrenList.add(taskSnapshot)
        }

        `when`(dataSnapshot.children).thenReturn(childrenList)

        return dataSnapshot
    }
}
