package com.example.aml

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.aml.model.UpdateProfileResponse
import com.example.aml.network.ApiClient
import com.example.aml.profile.UpdateProfileRequest
import okhttp3.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
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
    private lateinit var btnPickPhoto: ImageView

    private var uploadedPhotoUrl: String? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            uploadToCloudinary(it, this,
                onSuccess = { url ->
                    uploadedPhotoUrl = url
                    etPhoto.setText(url)
                    Glide.with(this).load(url).apply(RequestOptions.circleCropTransform()).into(btnPickPhoto)
                },
                onError = { error ->
                    Toast.makeText(this, "Upload error: $error", Toast.LENGTH_SHORT).show()
                })
        }
    }

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
        btnPickPhoto = findViewById(R.id.btnPickPhoto)

        etDateOfBirth.setOnClickListener {
            showDatePickerDialog()
        }

        btnPickPhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
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
        val userId = "5e27c94c-105c-4658-bf4a-d5a06a0b751e"
        val username = etFullName.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()
        val dateOfBirth = etDateOfBirth.text.toString()
        val profilePhotoUrl = uploadedPhotoUrl ?: etPhoto.text.toString()
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
                    finish()
                }
            }
        }
        handler.postDelayed(runnable, 1000)
    }

    private fun uploadToCloudinary(
        imageUri: Uri,
        context: Context,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(imageUri)
        val imageBytes = inputStream?.readBytes() ?: run {
            onError("Could not read image file.")
            return
        }

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", "profile.jpg", imageBytes.toRequestBody("image/*".toMediaTypeOrNull()))
            .addFormDataPart("upload_preset", "android_image") // Ganti dengan preset milikmu
            .build()

        val request = Request.Builder()
            .url("https://api.cloudinary.com/v1_1/delnb4i7e/image/upload") // Ganti dengan nama cloud kamu
            .post(requestBody)
            .build()

        OkHttpClient().newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                onError(e.message ?: "Unknown error")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val body = response.body?.string()
                if (response.isSuccessful && body != null) {
                    val json = JSONObject(body)
                    val imageUrl = json.getString("secure_url")
                    onSuccess(imageUrl)
                } else {
                    onError("Upload failed: ${response.code}")
                }
            }
        })
    }
}
