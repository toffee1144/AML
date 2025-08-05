package com.example.aml.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val success: Boolean,
    val token: String,
    val username: String,
    val userId: String,
    @SerializedName("profile_photo_url")
    val profilePhotoUrl: String?
)


