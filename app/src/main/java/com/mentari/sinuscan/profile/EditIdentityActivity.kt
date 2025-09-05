package com.mentari.sinuscan.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mentari.sinuscan.R
import com.mentari.sinuscan.model.CloudinaryUploadResponse
import com.mentari.sinuscan.model.UpdateProfileResponse
import com.mentari.sinuscan.network.ApiClient
import com.mentari.sinuscan.utility.FileUtil
import com.mentari.sinuscan.utility.SessionManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.core.content.ContextCompat
import com.mentari.sinuscan.model.UpdateProfileRequest
import com.mentari.sinuscan.model.UserProfileResponse
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class EditIdentityActivity : AppCompatActivity() {
    private val rfcFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private lateinit var etFullName: EditText
    private lateinit var etDateOfBirth: EditText
    private lateinit var rgSex: RadioGroup
    private lateinit var rbMale: RadioButton
    private lateinit var rbFemale: RadioButton
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var etPhoto: EditText
    private lateinit var btnPickPhoto: ImageView
    private lateinit var imgProfilePreview: ImageView
    private lateinit var btnUpdateIdentity: Button


    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val file = FileUtil.from(this, it)
            if (file != null) {
                uploadToCloudinary(file) { imageUrl ->
                    SessionManager.setProfilePhotoUrl(this@EditIdentityActivity, imageUrl)
                    etPhoto.setText(imageUrl)
                    Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_person_placeholder)
                        .into(imgProfilePreview)
                }
            } else {
                Toast.makeText(this, "Gagal membaca file gambar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_identity)

        etFullName = findViewById(R.id.etFullName)
        etDateOfBirth = findViewById(R.id.etDateOfBirth)
        rgSex = findViewById(R.id.rgSex)
        rbMale = findViewById(R.id.rbMale)
        rbFemale = findViewById(R.id.rbFemale)
        val tealColor = ContextCompat.getColorStateList(this, R.color.teal)
        rbMale.buttonTintList = tealColor
        rbFemale.buttonTintList = tealColor

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        etPhoto = findViewById(R.id.etPhoto)
        btnPickPhoto = findViewById(R.id.btnPickPhoto)
        imgProfilePreview = findViewById(R.id.imgProfilePreview)
        btnUpdateIdentity = findViewById(R.id.btnUpdateIdentity)

        etDateOfBirth.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    val selected = Calendar.getInstance().apply { set(year, month, day) }
                    etDateOfBirth.setText(dateFormat.format(selected.time))
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnPickPhoto.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        btnUpdateIdentity.setOnClickListener {
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()
            if (password != confirmPassword) {
                Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val gender = if (rbMale.isChecked) "Male" else "Female"

            val request = UpdateProfileRequest(
                userId = SessionManager.getUserId(this),
                username = etFullName.text.toString(),
                email = etEmail.text.toString(),
                password = password.takeIf { it.isNotEmpty() },
                dateOfBirth = etDateOfBirth.text.toString(),
                sex = gender,
                profilePhotoUrl = etPhoto.text.toString()
            )

            ApiClient.apiService.updateProfile(request)
                .enqueue(object : Callback<UpdateProfileResponse> {
                    override fun onResponse(call: Call<UpdateProfileResponse>, response: Response<UpdateProfileResponse>) {
                        if (response.isSuccessful && response.body() != null) {
                            Toast.makeText(this@EditIdentityActivity, "Berhasil disimpan", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@EditIdentityActivity, "Gagal menyimpan", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                        Toast.makeText(this@EditIdentityActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }


        loadUserProfile()
    }

    private fun loadUserProfile() {
        val userId = SessionManager.getUserId(this)
        ApiClient.apiService.getUserProfile(userId).enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!
                    etFullName.setText(user.username)
                    etEmail.setText(user.email)
                    etDateOfBirth.setText(
                        try {
                            val parsed = rfcFormat.parse(user.dateOfBirth)
                            dateFormat.format(parsed!!)
                        } catch (e: Exception) {
                            user.dateOfBirth // fallback: biarin apa adanya kalau sudah yyyy-MM-dd
                        }
                    )


                    if (user.sex == "Male") rbMale.isChecked = true
                    else if (user.sex == "Female") rbFemale.isChecked = true

                    user.profilePhotoUrl?.let {
                        etPhoto.setText(it)
                        Glide.with(this@EditIdentityActivity)
                            .load(it)
                            .placeholder(R.drawable.ic_person_placeholder)
                            .into(imgProfilePreview)
                    }
                } else {
                    Toast.makeText(this@EditIdentityActivity, "Gagal memuat profil", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Toast.makeText(this@EditIdentityActivity, "Gagal memuat profil: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun uploadToCloudinary(file: File, onSuccess: (String) -> Unit) {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val uploadPreset = MultipartBody.Part.createFormData("upload_preset", "aml_profile")

        ApiClient.cloudinaryService.uploadImage(body, uploadPreset)
            .enqueue(object : Callback<CloudinaryUploadResponse> {
                override fun onResponse(
                    call: Call<CloudinaryUploadResponse>,
                    response: Response<CloudinaryUploadResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        onSuccess(response.body()!!.secure_url)
                    } else {
                        Toast.makeText(this@EditIdentityActivity, "Upload gagal", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CloudinaryUploadResponse>, t: Throwable) {
                    Toast.makeText(this@EditIdentityActivity, "Upload error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
