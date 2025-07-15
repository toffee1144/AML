package com.example.aml.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aml.databinding.ActivityProfilIdentityBinding
import com.example.aml.utility.SessionManager
import com.example.aml.login.LoginActivity

class ProfilIdentityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilIdentityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilIdentityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        val username = SessionManager.getUsername(this) ?: "Guest Guest"
        val email = SessionManager.getEmail(this) ?: "guest@example.com"

        binding.tvUsername.text = username
        binding.tvEmail.text = email
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnIdentityDetails.setOnClickListener {
            // Pindah ke DetailProfilActivity
            startActivity(Intent(this, DetailProfilActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            SessionManager.clear(this)
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()

            // Pindah ke LoginActivity dan clear backstack
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
