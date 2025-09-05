package com.mentari.sinuscan.model

data class SignUpRequest(
    val userId: String,
    val nickname: String,
    val token: Int,
    val password: String
)

