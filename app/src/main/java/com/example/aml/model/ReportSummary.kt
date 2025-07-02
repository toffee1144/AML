package com.example.aml.model

data class ReportSummary(
    val percentage: Int,        // e.g., 50
    val matchCount: Int,        // e.g., 4
    val totalCount: Int,        // e.g., 8
    val explanation: String,
    val recommendation: String
)
