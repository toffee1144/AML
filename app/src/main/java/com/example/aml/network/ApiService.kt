package com.example.aml.network

import com.example.aml.model.FormData
import com.example.aml.model.LatestDataResponse
import com.example.aml.model.LoginRequest
import com.example.aml.model.LoginResponse
import com.example.aml.model.ReportItem
import com.example.aml.model.ReportSummary
import com.example.aml.model.SignUpRequest
import com.example.aml.model.SignUpResponse
import com.example.aml.model.UpdateProfileResponse
import com.example.aml.profile.UpdateProfileRequest
import com.example.aml.profile.UserProfileResponse

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Path
import retrofit2.http.PUT

interface ApiService {
    @POST("/register-guest")
    fun guestUser(@Body body: Map<String, String>): Call<Void>

    @POST("/register-user")
    fun registerUser(@Body request: SignUpRequest): Call<SignUpResponse>

    @POST("/submit-form") // Replace with actual endpoint
    fun submitForm(@Body formData: FormData): Call<Void>

    @GET("latest-data")
    fun getLatestData(@Query("userId") userId: String): Call<LatestDataResponse>

    @GET("/get-report/{uid}")
    fun getReportByUid(@Path("uid") uid: String): Call<FormData>

    @GET("get-reports/{user_id}")
    fun getReports(@Path("user_id") userId: String): Call<List<ReportItem>>

    @GET("get-report-summary/{uid}")
    fun getReportSummary(@Path("uid") uid: String): Call<ReportSummary>

    @POST("login") // Replace with your actual endpoint
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @PUT("update-profile")
    fun updateProfile(@Body request: UpdateProfileRequest): Call<UpdateProfileResponse>

    @GET("get-profile/{id}")
    fun getUserProfile(@Path("id") userId: String): Call<UserProfileResponse>

}

