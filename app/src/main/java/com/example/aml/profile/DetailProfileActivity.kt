package com.example.aml

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.aml.profile.UserProfileResponse
import com.example.aml.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailProfilActivity : AppCompatActivity() {

    private lateinit var tvFullName: TextView
    private lateinit var tvDOB: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvPhotoName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPassword: TextView
    private lateinit var imgProfile: ImageView
    private lateinit var btnEditIdentity: Button
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_profil)

        // Bind layout
        tvFullName = findViewById(R.id.tvFullName)
        tvDOB = findViewById(R.id.tvDOB)
        tvGender = findViewById(R.id.tvGender)
        tvPhotoName = findViewById(R.id.tvPhotoName)
        tvEmail = findViewById(R.id.tvEmail)
        tvPassword = findViewById(R.id.tvPassword)
        imgProfile = findViewById(R.id.imgProfile)
        btnEditIdentity = findViewById(R.id.btnEditIdentity)
        btnBack = findViewById(R.id.btnBack)

        // Fetch dan tampilkan profil
        fetchUserProfile()

        // Navigasi ke EditIdentityActivity
        btnEditIdentity.setOnClickListener {
            startActivity(Intent(this, EditIdentityActivity::class.java))
        }

        // Kembali
        btnBack.setOnClickListener {
            finish()
        }
    }
    override fun onResume() {
        super.onResume()
        fetchUserProfile() // Refresh data setiap kembali ke activity ini
    }

    private fun fetchUserProfile() {
        val userId = "5e27c94c-105c-4658-bf4a-d5a06a0b751e" // Ganti dengan SessionManager jika tersedia

        ApiClient.apiService.getUserProfile(userId)
            .enqueue(object : Callback<UserProfileResponse> {
                override fun onResponse(
                    call: Call<UserProfileResponse>,
                    response: Response<UserProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        user?.let {
                            tvFullName.text = it.username
                            tvDOB.text = it.dateOfBirth
                            tvGender.text = it.sex
                            tvPhotoName.text = it.profilePhotoUrl.substringAfterLast("/")
                            tvEmail.text = it.email
                            tvPassword.text = "********"

                            // Jika ada Glide, tampilkan profil URL
                            Glide.with(this@DetailProfilActivity)
                                .load(it.profilePhotoUrl)
                                .placeholder(R.drawable.ic_login)
                                .into(imgProfile)
                        }
                    } else {
                        Toast.makeText(this@DetailProfilActivity, "Gagal ambil profil", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                    Toast.makeText(this@DetailProfilActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
