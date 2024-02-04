package com.kairon007.pestotasks.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.kairon007.pestotasks.repository.AuthRepository
import com.kairon007.pestotasks.viewmodel.RegistrationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.anyOrNull

@ExperimentalCoroutinesApi
class RegistrationViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testDispatcher)

    @Mock
    lateinit var authRepository: AuthRepository

    @Mock
    lateinit var registrationResultObserver: Observer<Boolean>

    private lateinit var registrationViewModel: RegistrationViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        reset(authRepository, registrationResultObserver)
        registrationViewModel = RegistrationViewModel(authRepository)
        registrationViewModel.registrationResult.observeForever(registrationResultObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testCoroutineScope.cleanupTestCoroutines()
    }

    @Test
    fun `registerUser calls authRepository and updates registrationResult`() {
        // Given
        val email = "test@example.com"
        val password = "password"
        val isSuccess = true

        `when`(authRepository.registerUser(anyString(), anyString(), anyOrNull())).thenAnswer {
            val callback: (Boolean) -> Unit = it.getArgument(2)
            callback.invoke(isSuccess)
        }

        // When
        runBlocking {
            registrationViewModel.registerUser(email, password)
        }

        // Then
        verify(authRepository).registerUser(anyString(), anyString(), anyOrNull())
        verify(registrationResultObserver).onChanged(isSuccess)
        assert(registrationViewModel.registrationResult.value == isSuccess)
    }
}
