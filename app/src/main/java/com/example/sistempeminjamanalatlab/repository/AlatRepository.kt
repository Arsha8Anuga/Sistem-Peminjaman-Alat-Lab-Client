package com.example.sistempeminjamanalatlab.repository

import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.models.entity.Alat
import com.example.sistempeminjamanalatlab.models.request.AlatCreateRequest
import com.example.sistempeminjamanalatlab.models.request.AlatUpdateRequest
import com.example.sistempeminjamanalatlab.models.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlatRepository(private val apiService: APIService) {

    // Helper untuk mempermudah pengambilan Token (bisa dari SessionManager)
    // Untuk sementara kita masukkan via parameter fungsi

    // --- FITUR MAHASISWA & UMUM ---

    fun getAllAlat(token: String, onResult: (List<Alat>?) -> Unit) {
        apiService.getAllAlat("Bearer $token").enqueue(object : Callback<AlatListResponse> {
            override fun onResponse(call: Call<AlatListResponse>, response: Response<AlatListResponse>) {
                // Ambil data di dalam body.data (List<Alat>)
                onResult(response.body()?.data)
            }
            override fun onFailure(call: Call<AlatListResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun getAlatDetail(token: String, id: Long, onResult: (Alat?) -> Unit) {
        apiService.getAlatById("Bearer $token", id).enqueue(object : Callback<AlatResponse> {
            override fun onResponse(call: Call<AlatResponse>, response: Response<AlatResponse>) {
                onResult(response.body()?.data)
            }
            override fun onFailure(call: Call<AlatResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }

    // --- FITUR LABORAN (ADMINISTRASI INVENTARIS) ---

    fun addAlat(token: String, request: AlatCreateRequest, onResult: (Boolean, String?) -> Unit) {
        apiService.createAlat("Bearer $token", request).enqueue(object : Callback<AlatResponse> {
            override fun onResponse(call: Call<AlatResponse>, response: Response<AlatResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<AlatResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }

    fun updateAlat(token: String, id: Long, request: AlatUpdateRequest, onResult: (Boolean, String?) -> Unit) {
        apiService.updateAlat("Bearer $token", id, request).enqueue(object : Callback<AlatResponse> {
            override fun onResponse(call: Call<AlatResponse>, response: Response<AlatResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<AlatResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }

    fun deleteAlat(token: String, id: Long, onResult: (Boolean, String?) -> Unit) {
        apiService.deleteAlat("Bearer $token", id).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }
}