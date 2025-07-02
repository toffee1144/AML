package com.example.aml.model

data class SignUpRequest(
    val userId: String,
    val username: String,
    val email: String,
    val password: String
)

