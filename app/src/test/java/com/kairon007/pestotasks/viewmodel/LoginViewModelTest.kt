package com.kairon007.pestotasks.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kairon007.pestotasks.repository.AuthRepository
import com.kairon007.pestotasks.viewmodel.LoginViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.anyOrNull

class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var authRepository: AuthRepository

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        loginViewModel = LoginViewModel(authRepository)
    }

    @Test
    fun `loginUser calls authRepository and updates loginResult`() {
        // Given
        val email = "test@example.com"
        val password = "password"
        val isSuccess = true
        `when`(authRepository.loginUser(anyString(), anyString(), anyOrNull())).thenAnswer {
            val callback: (Boolean) -> Unit = it.getArgument(2)
            callback.invoke(isSuccess)
        }

        // When
        loginViewModel.loginUser(email, password)

        // Then
        verify(authRepository).loginUser(anyString(), anyString(), anyOrNull())
        assert(loginViewModel.loginResult.value == isSuccess)




    }
}
