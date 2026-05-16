package com.example.sistempeminjamanalatlab.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.sistempeminjamanalatlab.models.response.*
import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.models.request.LoginRequest

class AuthRepository(private val apiService: APIService) {
    fun login(request: LoginRequest, onResult: (LoginData?, String?) -> Unit) {
        apiService.loginUser(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                onResult(response.body()?.data, response.body()?.message)
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onResult(null, t.message)
            }
        })
    }

    /*fun register(request: UserCreateRequest, onResult: (Boolean, String?) -> Unit) {
        apiService.registerUser(request).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }*/
}