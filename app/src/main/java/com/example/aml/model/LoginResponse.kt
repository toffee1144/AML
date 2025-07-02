package com.example.aml.model

data class LoginResponse(
    val success: Boolean,
    val token: String,
    val username: String,
    val userId: String,
)

