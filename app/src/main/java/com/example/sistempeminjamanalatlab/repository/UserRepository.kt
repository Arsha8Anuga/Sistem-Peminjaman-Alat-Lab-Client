package com.example.sistempeminjamanalatlab.repository

import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.models.entity.User
import com.example.sistempeminjamanalatlab.models.request.LoginRequest
import com.example.sistempeminjamanalatlab.models.request.UserUpdateRequest
import com.example.sistempeminjamanalatlab.models.request.UserCreateRequest
import com.example.sistempeminjamanalatlab.models.response.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val apiService: APIService) {
    // Digunakan Admin untuk List User
    fun getAllUsers(token: String, onResult: (List<User>?, String?) -> Unit) {
        apiService.getAllUsers("Bearer $token").enqueue(object : Callback<UserListResponse> {
            override fun onResponse(call: Call<UserListResponse>, response: Response<UserListResponse>) {
                onResult(response.body()?.data, response.body()?.message)
            }
            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                onResult(null, t.message)
            }
        })
    }

    // Digunakan untuk halaman Profil
    fun getUserById(token: String, id: Long, onResult: (User?, String?) -> Unit) {
        apiService.getUserById("Bearer $token", id).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                onResult(response.body()?.data, response.body()?.message)
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                onResult(null, t.message)
            }
        })
    }

    // Update Foto Profil
    fun uploadProfilePhoto(token: String, id: Long, photo: MultipartBody.Part, onResult: (Boolean, String?) -> Unit) {
        apiService.uploadUserPhoto("Bearer $token", id, photo).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }
}