package com.example.sistempeminjamanalatlab.repository

import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.models.entity.User
import com.example.sistempeminjamanalatlab.models.request.LoginRequest
import com.example.sistempeminjamanalatlab.models.request.UserUpdateRequest
import com.example.sistempeminjamanalatlab.models.request.UserCreateRequest
import com.example.sistempeminjamanalatlab.models.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val apiService: APIService) {

    // --- AUTHENTICATION ---

    fun login(request: LoginRequest, onResult: (LoginData?, String?) -> Unit) {
        apiService.loginUser(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                // Mengembalikan data login (token, role, id) dan message
                onResult(response.body()?.data, response.body()?.message)
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onResult(null, t.message)
            }
        })
    }

    // --- USER MANAGEMENT (KHUSUS ADMIN/STAFF) ---

    fun getAllUsers(token: String, onResult: (List<User>?) -> Unit) {
        apiService.getAllUsers("Bearer $token").enqueue(object : Callback<UserListResponse> {
            override fun onResponse(call: Call<UserListResponse>, response: Response<UserListResponse>) {
                onResult(response.body()?.data)
            }
            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }

    // Menambah User Baru (Oleh Admin)
    fun createUser(token: String, request: UserCreateRequest, onResult: (Boolean, String?) -> Unit) {
        // Asumsikan kita menambah endpoint ini di APIService
        apiService.createUser("Bearer $token", request).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }

    fun updateUser(token: String, id: Long, request: UserUpdateRequest, onResult: (Boolean, String?) -> Unit) {
        apiService.updateUser("Bearer $token", id, request).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }

    fun deleteUser(token: String, id: Long, onResult: (Boolean, String?) -> Unit) {
        apiService.deleteUser("Bearer $token", id).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }

    // --- PROFILE ---

    fun getUserProfile(token: String, userId: Long, onResult: (User?) -> Unit) {
        apiService.getUserById("Bearer $token", userId).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                onResult(response.body()?.data)
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }
}