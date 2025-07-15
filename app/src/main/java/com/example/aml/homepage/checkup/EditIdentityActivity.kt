package com.example.aml.profile

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aml.databinding.ActivityEditIdentityBinding
import com.example.aml.utility.SessionManager

class EditIdentityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditIdentityBinding
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditIdentityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupInitialData()
        setupListeners()
    }

    private fun setupInitialData() {
        // Prefill from session (TODO: perbanyak field kalau backend sudah mendukung)
        binding.etFullName.setText(SessionManager.getUsername(this))
        binding.etEmail.setText(SessionManager.getEmail(this))
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnUpdateIdentity.setOnClickListener {
            val fullName = binding.etFullName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val dob = binding.etDateOfBirth.text.toString().trim() // TODO
            val password = binding.etPassword.text.toString().trim() // TODO
            val confirmPassword = binding.etConfirmPassword.text.toString().trim() // TODO

            // Basic validation
            if (fullName.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Full name and email must be filled", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // TODO: Add real API call to update identity

            // Simulate update success
            SessionManager.saveUsername(this, fullName)
            SessionManager.saveEmail(this, email)

            showSuccessOverlay()
        }
    }

    private fun showSuccessOverlay() {
        binding.successOverlay.visibility = View.VISIBLE

        countDownTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                binding.tvRedirectCountdown.text = "Direct in ${secondsLeft}s"
            }

            override fun onFinish() {
                // Hide overlay
                binding.successOverlay.visibility = View.GONE

                // Back to previous activity (could be ProfileIdentityActivity)
                finish()
            }
        }

        countDownTimer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }
}
