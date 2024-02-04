package com.kairon007.pestotasks.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kairon007.pestotasks.R
import com.kairon007.pestotasks.databinding.ActivityRegistrationBinding
import com.kairon007.pestotasks.viewmodel.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private val registrationViewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.registration)
        binding.btnRegister.setOnClickListener {
            val email = binding.editRegisterEmail.text.toString()
            val password = binding.editRegisterPassword.text.toString()
            binding.textInputLayoutEmail.error = null
            binding.textInputLayoutPassword.error = null
            if (isValidEmail(email) && isValidPassword(password)) {
                registrationViewModel.registerUser(email, password)
            }

        }

        binding.btnGoToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Observe the registration result
        registrationViewModel.registrationResult.observe(this) { isSuccess ->
            if (isSuccess) {
                // Registration successful
                Toast.makeText(this, getString(R.string.reg_success), Toast.LENGTH_SHORT).show()
                // Example: Navigate to the main activity or login activity
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                // Registration failed
                Toast.makeText(this, getString(R.string.reg_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun isValidEmail(email: String): Boolean {
        return if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            true
        } else {
            binding.textInputLayoutEmail.error = getString(R.string.valid_email)
            false
        }
    }

    private fun isValidPassword(password: String): Boolean {
        return if (password.length >= 6) {
            true
        } else {
            binding.textInputLayoutPassword.error = getString(R.string.password_valid)
            false
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
