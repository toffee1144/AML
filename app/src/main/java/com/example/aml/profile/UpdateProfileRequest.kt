package com.example.aml.profile

data class UpdateProfileRequest(
    val userId: String,
    val username: String?,
    val email: String?,
    val password: String?,
    val dateOfBirth: String?,
    val sex: String?,
    val profilePhotoUrl: String?
)
