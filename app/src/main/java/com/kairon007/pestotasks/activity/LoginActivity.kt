package com.kairon007.pestotasks.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kairon007.pestotasks.R
import com.kairon007.pestotasks.databinding.LoginActivityBinding
import com.kairon007.pestotasks.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title= getString(R.string.login)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Set up click listeners
        binding.btnLogin.setOnClickListener {
            loginViewModel.loginUser(
                binding.editEmail.text.toString(),
                binding.editPassword.text.toString()
            )
        }

        binding.btnGoToRegistration.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
        }

        // Observe the login result
        loginViewModel.loginResult.observe(this) { isSuccess ->
            if (isSuccess) {
                // Login successful
                Toast.makeText(this, getString(R.string.login_successful), Toast.LENGTH_SHORT).show()
                // Example: Navigate to the main activity
                startActivity(Intent(this, TaskListActivity::class.java))
                finish()
            } else {
                // Login failed
                Toast.makeText(this, getString(R.string.authentication_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
