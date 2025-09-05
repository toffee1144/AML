package com.mentari.sinuscan.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mentari.sinuscan.databinding.ActivityProfilIdentityBinding
import com.mentari.sinuscan.utility.SessionManager
import com.mentari.sinuscan.login.LoginActivity
import com.bumptech.glide.Glide
import com.mentari.sinuscan.R // penting

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
        val profilePhotoUrl = SessionManager.getProfilePhotoUrl(this)

        binding.tvUsername.text = username
        binding.tvEmail.text = email

        // Logging untuk debug URL
        android.util.Log.d("ProfilActivity", "Username: $username")
        android.util.Log.d("ProfilActivity", "Email: $email")
        android.util.Log.d("ProfilActivity", "Photo URL: $profilePhotoUrl")

        Glide.with(this)
            .load(profilePhotoUrl)
            .placeholder(R.drawable.ic_person_placeholder)
            .error(R.drawable.ic_person_placeholder)
            .circleCrop()
            .into(binding.imgProfilePicture)
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
