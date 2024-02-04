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
        // Set up click listeners
        binding.btnRegister.setOnClickListener {
            registrationViewModel.registerUser(
                binding.editRegisterEmail.text.toString(),
                binding.editRegisterPassword.text.toString()
            )
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
