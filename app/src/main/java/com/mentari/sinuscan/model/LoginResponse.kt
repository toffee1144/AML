package com.mentari.sinuscan.model

data class LoginResponse(
    val userId: String,
    val nickname: String,
    val token: String,
    val profilePhotoUrl: String? = null
)

