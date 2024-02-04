package com.kairon007.pestotasks.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class AuthRepositoryTest {
    @Mock
    lateinit var authResult: Task<AuthResult>

    @Mock
    lateinit var authUser: FirebaseUser

    @Mock
    private lateinit var auth: FirebaseAuth

    private lateinit var authRepository: AuthRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        authRepository = AuthRepository(auth)
    }

    @Test
    fun `test login with valid credentials`() {
        // Mock successful sign-in
        `when`(
            auth.signInWithEmailAndPassword(
                "valid@email.com",
                "password"
            )
        ).thenReturn(authResult)

        // Call the method to be tested
        authRepository.loginUser("valid@email.com", "password") { result ->
            // Verify that the callback is called with true
            assert(result)
        }

        // Verify that signInWithEmailAndPassword is called with the correct arguments
        verify(auth).signInWithEmailAndPassword("valid@email.com", "password")
    }


    @Test
    fun `test logout`() {
        // Call the method to be tested
        authRepository.logout()

        // Verify that signOut is called
        verify(auth).signOut()
    }

    @Test
    fun `test getCurrentUser when user is not null`() {
        // Mock a non-null current user
        `when`(auth.currentUser).thenReturn(authUser)

        // Call the method to be tested
        val result = authRepository.getCurrentUser()

        // Verify that the result is true
        assert(result)
    }

    @Test
    fun `test getCurrentUser when user is null`() {
        // Mock a null current user
        `when`(auth.currentUser).thenReturn(null)

        // Call the method to be tested
        val result = authRepository.getCurrentUser()

        // Verify that the result is false
        assert(!result)
    }

    @Test
    fun `test registerUser with valid credentials`() {
        // Mock successful user registration
        `when`(auth.createUserWithEmailAndPassword("valid@email.com", "password")).thenReturn(
            authResult
        )

        // Call the method to be tested
        authRepository.registerUser("valid@email.com", "password") { result ->
            // Verify that the callback is called with true
            assert(result)
        }

        // Verify that createUserWithEmailAndPassword is called with the correct arguments
        verify(auth).createUserWithEmailAndPassword("valid@email.com", "password")
    }

}