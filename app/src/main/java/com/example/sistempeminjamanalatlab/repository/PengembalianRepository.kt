package com.example.sistempeminjamanalatlab.repository

import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.models.entity.Pengembalian
import com.example.sistempeminjamanalatlab.models.request.PengembalianRequest
import com.example.sistempeminjamanalatlab.models.request.VerifyPengembalianRequest
import com.example.sistempeminjamanalatlab.models.response.BaseResponse
import com.example.sistempeminjamanalatlab.models.response.PengembalianResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PengembalianRepository(private val apiService: APIService) {

    /**
     * Sesuai APIService: @GET("pengembalian/{peminjaman_id}")
     * Mengambil detail data pengembalian.
     */
    fun getDetailPengembalian(token: String, peminjamanId: Long, onResult: (Pengembalian?, String?) -> Unit) {
        apiService.getDetailPengembalian("Bearer $token", peminjamanId).enqueue(object : Callback<PengembalianResponse> {
            override fun onResponse(call: Call<PengembalianResponse>, response: Response<PengembalianResponse>) {
                if (response.isSuccessful) {
                    onResult(response.body()?.data, response.body()?.message)
                } else {
                    onResult(null, "Gagal memuat detail pengembalian")
                }
            }

            override fun onFailure(call: Call<PengembalianResponse>, t: Throwable) {
                onResult(null, t.message)
            }
        })
    }

    /**
     * Sesuai APIService: @POST("pengembalian/")
     * Mahasiswa membuat pengajuan pengembalian.
     */
    fun submitPengembalian(token: String, request: PengembalianRequest, onResult: (Boolean, String?) -> Unit) {
        apiService.createPengembalian("Bearer $token", request).enqueue(object : Callback<PengembalianResponse> {
            override fun onResponse(call: Call<PengembalianResponse>, response: Response<PengembalianResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }

            override fun onFailure(call: Call<PengembalianResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }

    /**
     * Sesuai APIService: @PUT("pengembalian/{peminjaman_id}/verify")
     * Laboran melakukan verifikasi pengembalian.
     * * Catatan: Jika di APIService kamu belum ada @Body untuk mengirim denda/status,
     * pastikan APIService-nya diupdate dulu agar bisa menerima VerifyPengembalianRequest.
     */
    fun verifyPengembalian(token: String, peminjamanId: Long, request: VerifyPengembalianRequest, onResult: (Boolean, String?) -> Unit) {
        // Asumsi: APIService.verifyPengembalian sudah ditambahkan parameter @Body request: VerifyPengembalianRequest
        apiService.verifyPengembalian("Bearer $token", peminjamanId, request).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }
}