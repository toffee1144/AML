package com.mentari.sinuscan.model

data class LoginRequest(
    val nickname: String,
    val token: String,
    val password: String
)
