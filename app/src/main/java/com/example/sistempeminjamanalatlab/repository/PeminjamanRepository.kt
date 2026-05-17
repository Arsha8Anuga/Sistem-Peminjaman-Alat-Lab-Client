package com.example.sistempeminjamanalatlab.repository

import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.models.entity.Peminjaman
import com.example.sistempeminjamanalatlab.models.request.*
import com.example.sistempeminjamanalatlab.models.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PeminjamanRepository(private val apiService: APIService) {

    // --- FITUR MAHASISWA ---

    // Submit Pengajuan Baru
    fun submitPeminjaman(token: String, request: PeminjamanRequest, onResult: (Boolean, String?) -> Unit) {
        apiService.createPeminjaman(token, request).enqueue(object : Callback<PeminjamanResponse> {
            override fun onResponse(call: Call<PeminjamanResponse>, response: Response<PeminjamanResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<PeminjamanResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }

    // Ambil Riwayat Milik Sendiri (Mahasiswa)
    fun getMyLoanHistory(token: String, onResult: (List<Peminjaman>?, String?) -> Unit) {
        apiService.getListPeminjaman(token).enqueue(object : Callback<PeminjamanListResponse> {
            override fun onResponse(call: Call<PeminjamanListResponse>, response: Response<PeminjamanListResponse>) {
                onResult(response.body()?.data, response.body()?.message)
            }
            override fun onFailure(call: Call<PeminjamanListResponse>, t: Throwable) {
                onResult(null, t.message)
            }
        })
    }

    // Ambil Detail Peminjaman (Untuk melihat item alat di dalamnya)
    fun getLoanDetail(token: String, loanId: Long, onResult: (Peminjaman?, String?) -> Unit) {
        apiService.getDetailPeminjaman(token, loanId).enqueue(object : Callback<PeminjamanResponse> {
            override fun onResponse(call: Call<PeminjamanResponse>, response: Response<PeminjamanResponse>) {
                onResult(response.body()?.data, response.body()?.message)
            }
            override fun onFailure(call: Call<PeminjamanResponse>, t: Throwable) {
                onResult(null, t.message)
            }
        })
    }

    fun cancelPeminjaman(token: String, loanId: Long, onResult: (Boolean, String?) -> Unit) {
        apiService.cancelPeminjaman(token, loanId).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                onResult(response.isSuccessful, response.body()?.message ?: "Peminjaman berhasil dibatalkan")
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }

    // --- FITUR LABORAN (APPROVAL & ALUR FISIK) ---

    // Aksi: Approve
    fun approveLoan(token: String, loanId: Long, onResult: (Boolean, String?) -> Unit) {
        apiService.approvePeminjaman(token, loanId).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) { onResult(false, t.message) }
        })
    }

    // Aksi: Reject
    fun rejectLoan(token: String, loanId: Long, onResult: (Boolean, String?) -> Unit) {
        apiService.rejectPeminjaman(token, loanId).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) { onResult(false, t.message) }
        })
    }

    // Aksi: Ambil Alat (Update status saat barang fisik diserahkan ke mhs)
    fun ambilAlat(token: String, loanId: Long, onResult: (Boolean, String?) -> Unit) {
        apiService.ambilAlat(token, loanId).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) { onResult(false, t.message) }
        })
    }
}