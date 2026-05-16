package com.example.sistempeminjamanalatlab.repository

import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.models.response.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val apiService: APIService) {

    // Update Foto Profil -> Tetap dipertahankan karena sudah sesuai dengan @POST("upload/user/{user_id}")
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