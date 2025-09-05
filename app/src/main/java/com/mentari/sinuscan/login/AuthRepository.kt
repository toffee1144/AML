package com.mentari.sinuscan.login

import com.mentari.sinuscan.model.LoginRequest
import com.mentari.sinuscan.model.LoginResponse
import com.mentari.sinuscan.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository {

    fun login(
        request: LoginRequest,
        onSuccess: (LoginResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val call = ApiClient.apiService.login(request)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    onSuccess(response.body()!!)
                } else {
                    onError("Login failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onError("Network error: ${t.localizedMessage}")
            }
        })
    }
}
