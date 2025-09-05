package com.mentari.sinuscan.network

import com.mentari.sinuscan.model.CloudinaryUploadResponse
import com.mentari.sinuscan.model.FormData
import com.mentari.sinuscan.model.LatestDataResponse
import com.mentari.sinuscan.model.LoginRequest
import com.mentari.sinuscan.model.LoginResponse
import com.mentari.sinuscan.model.ReportItem
import com.mentari.sinuscan.model.ReportSummary
import com.mentari.sinuscan.model.SignUpRequest
import com.mentari.sinuscan.model.SignUpResponse
import com.mentari.sinuscan.model.UpdateProfileResponse
import com.mentari.sinuscan.model.UpdateProfileRequest
import com.mentari.sinuscan.model.UserProfileResponse
import okhttp3.MultipartBody

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Path
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.DELETE

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

    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @DELETE("delete-report/{report_uid}")
    fun deleteReport(@Path("report_uid") reportUid: String): Call<Void>

    @PUT("update-profile")
    fun updateProfile(@Body request: UpdateProfileRequest): Call<UpdateProfileResponse>

    @GET("get-profile/{id}")
    fun getUserProfile(@Path("id") userId: String): Call<UserProfileResponse>

    @Multipart
    @POST("https://api.cloudinary.com/v1_1/delnb4i7e/image/upload")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part upload_preset: MultipartBody.Part
    ): Call<CloudinaryUploadResponse>

}

