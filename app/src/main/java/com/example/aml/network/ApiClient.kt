package com.example.aml.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://vghkr2cv-5000.asse.devtunnels.ms"
    private const val CLOUDINARY_URL = "https://api.cloudinary.com/v1_1/delnb4i7e/" // Ganti sesuai akun kamu

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    // Retrofit khusus untuk Cloudinary
    private val cloudinaryRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(CLOUDINARY_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val cloudinaryService: ApiService by lazy {
        cloudinaryRetrofit.create(ApiService::class.java)
    }
}
