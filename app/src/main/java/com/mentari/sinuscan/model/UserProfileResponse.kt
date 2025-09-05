package com.mentari.sinuscan.model

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    val id: String,
    val username: String,
    val email: String,

    @SerializedName("date_of_birth")
    val dateOfBirth: String,

    val sex: String,

    @SerializedName("profile_photo_url")
    val profilePhotoUrl: String?
)

