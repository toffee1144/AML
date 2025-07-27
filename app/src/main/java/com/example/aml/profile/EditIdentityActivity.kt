package com.example.aml

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.aml.model.UpdateProfileResponse
import com.example.aml.network.ApiClient
import com.example.aml.profile.UpdateProfileRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EditIdentityActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etDateOfBirth: EditText
    private lateinit var etPhoto: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var rgSex: RadioGroup
    private lateinit var rbMale: RadioButton
    private lateinit var rbFemale: RadioButton
    private lateinit var btnUpdateIdentity: Button
    private lateinit var successOverlay: LinearLayout
    private lateinit var tvRedirectCountdown: TextView
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_identity)

        etFullName = findViewById(R.id.etFullName)
        etDateOfBirth = findViewById(R.id.etDateOfBirth)
        etPhoto = findViewById(R.id.etPhoto)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        rgSex = findViewById(R.id.rgSex)
        rbMale = findViewById(R.id.rbMale)
        rbFemale = findViewById(R.id.rbFemale)
        btnUpdateIdentity = findViewById(R.id.btnUpdateIdentity)
        successOverlay = findViewById(R.id.successOverlay)
        tvRedirectCountdown = findViewById(R.id.tvRedirectCountdown)
        btnBack = findViewById(R.id.btnBack)

        etDateOfBirth.setOnClickListener {
            showDatePickerDialog()
        }

        btnUpdateIdentity.setOnClickListener {
            updateProfile()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, y, m, d ->
            val formatted = String.format("%04d-%02d-%02d", y, m + 1, d)
            etDateOfBirth.setText(formatted)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun updateProfile() {
        val userId = "5e27c94c-105c-4658-bf4a-d5a06a0b751e" // Ganti dengan ID asli dari session atau intent
        val username = etFullName.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()
        val dateOfBirth = etDateOfBirth.text.toString()
        val profilePhotoUrl = etPhoto.text.toString()
        val sex = if (rbMale.isChecked) "Male" else "Female"

        if (password != confirmPassword) {
            Toast.makeText(this, "Password and confirmation do not match", Toast.LENGTH_SHORT).show()
            return
        }

        val request = UpdateProfileRequest(
            userId = userId,
            username = username,
            email = email,
            password = password,
            dateOfBirth = dateOfBirth,
            sex = sex,
            profilePhotoUrl = profilePhotoUrl
        )

        ApiClient.apiService.updateProfile(request).enqueue(object : Callback<UpdateProfileResponse> {
            override fun onResponse(
                call: Call<UpdateProfileResponse>,
                response: Response<UpdateProfileResponse>
            ) {
                if (response.isSuccessful) {
                    showSuccessOverlay()
                } else {
                    Toast.makeText(this@EditIdentityActivity, "Update failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                Toast.makeText(this@EditIdentityActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showSuccessOverlay() {
        successOverlay.visibility = View.VISIBLE
        var seconds = 3
        tvRedirectCountdown.text = "Redirect in ${seconds}s"

        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                seconds--
                if (seconds > 0) {
                    tvRedirectCountdown.text = "Redirect in ${seconds}s"
                    handler.postDelayed(this, 1000)
                } else {
                    finish() // Kembali ke activity sebelumnya
                }
            }
        }
        handler.postDelayed(runnable, 1000)
    }
}