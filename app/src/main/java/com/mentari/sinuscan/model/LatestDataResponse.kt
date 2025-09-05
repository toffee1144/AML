package com.mentari.sinuscan.model

data class LatestDataResponse(
    val percentage: Int,
    val recommendedAction: String,
    val report: String,
    val userId: String
)
