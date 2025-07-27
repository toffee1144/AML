package com.example.aml.profile

data class UserProfileResponse(
    val id: String,
    val username: String,
    val email: String,
    val dateOfBirth: String,
    val sex: String,
    val profilePhotoUrl: String
)

