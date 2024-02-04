package com.kairon007.pestotasks.repository

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthRepository @Inject constructor(private val auth: FirebaseAuth) {

    fun loginUser(email: String, password: String, callback: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    callback(true)
                } else {
                    // If sign in fails, display a message to the user.
                    callback(false)
                }
            }
    }
    fun getCurrentUser(): Boolean {
        return auth.currentUser != null
    }
    fun registerUser(email: String, password: String, callback: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registration success
                    callback(true)
                } else {
                    // If registration fails, display a message to the user.
                    callback(false)
                }
            }
    }
}
