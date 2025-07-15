package com.example.aml.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aml.R
import com.example.aml.databinding.ActivityDetailProfilBinding
import com.example.aml.utility.SessionManager

class DetailProfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProfilBinding
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupListeners()
    }

    private fun setupView() {
        val username = SessionManager.getUsername(this) ?: "Guest Guest"
        val email = SessionManager.getEmail(this) ?: "guest@example.com"

        // Set teks berdasarkan data session
        binding.tvTitle.text = "Identity Details"
        binding.tvEmail.text = email
        binding.tvPhotoName.text = "Profile.jpg" // hardcoded, bisa kamu ubah kalau ambil dari storage/server

        // Contoh static name dan gender, kamu bisa ubah kalau sudah ada datanya
        binding.apply {
            // Full name, DOB, and gender text
            // Kalau nanti ambil dari server, update ini pakai ViewModel atau data tambahan dari SessionManager
            // Contoh:
            // tvFullName.text = nameFromServer
            // tvDOB.text = dobFromServer
            // tvGender.text = genderFromServer
        }
    }

    private fun setupListeners() {
        // Tombol kembali
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Tombol show/hide password
        binding.btnShowPassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            binding.tvPassword.text = if (isPasswordVisible) "password123" else "********"
            // ganti icon jika mau
        }

        // Tombol edit identitas
        binding.btnEditIdentity.setOnClickListener {
            Toast.makeText(this, "Edit identity clicked", Toast.LENGTH_SHORT).show()
            // TODO: Pindah ke halaman edit jika sudah dibuat
        }
    }
}
