package com.example.sistempeminjamanalatlab.repository

import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.models.entity.Peminjaman
import com.example.sistempeminjamanalatlab.models.entity.Pengembalian
import com.example.sistempeminjamanalatlab.models.request.*
import com.example.sistempeminjamanalatlab.models.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PeminjamanRepository(private val apiService: APIService) {

    // --- FITUR MAHASISWA ---

    // 1. Kirim Form Pinjam
    fun submitPeminjaman(token: String, request: PinjamRequest, onResult: (Boolean, String?) -> Unit) {
        apiService.createPeminjaman("Bearer $token", request).enqueue(object : Callback<PeminjamanResponse> {
            override fun onResponse(call: Call<PeminjamanResponse>, response: Response<PeminjamanResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<PeminjamanResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }

    // 2. Riwayat Peminjaman (Milik Mahasiswa yang sedang login)
    fun getMyLoanHistory(token: String, onResult: (List<Peminjaman>?) -> Unit) {
        apiService.getMyPeminjaman("Bearer $token").enqueue(object : Callback<PeminjamanListResponse> {
            override fun onResponse(call: Call<PeminjamanListResponse>, response: Response<PeminjamanListResponse>) {
                onResult(response.body()?.data) // Langsung ambil List<Peminjaman>
            }
            override fun onFailure(call: Call<PeminjamanListResponse>, t: Throwable) { onResult(null) }
        })
    }

    // --- FITUR LABORAN (APPROVAL & VERIFIKASI) ---

    // 3. List Pengajuan (Filter status: pending)
    fun getPendingLoans(token: String, onResult: (List<Peminjaman>?) -> Unit) {
        apiService.getAllPeminjaman("Bearer $token", "pending").enqueue(object : Callback<PeminjamanListResponse> {
            override fun onResponse(call: Call<PeminjamanListResponse>, response: Response<PeminjamanListResponse>) {
                onResult(response.body()?.data)
            }
            override fun onFailure(call: Call<PeminjamanListResponse>, t: Throwable) { onResult(null) }
        })
    }

    // 4. Update Status (Approval: Setujui/Tolak)
    fun approvalPeminjaman(token: String, loanId: Long, request: ApprovalRequest, onResult: (Boolean, String?) -> Unit) {
        apiService.approvalPeminjaman("Bearer $token", loanId, request).enqueue(object : Callback<PeminjamanResponse> {
            override fun onResponse(call: Call<PeminjamanResponse>, response: Response<PeminjamanResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<PeminjamanResponse>, t: Throwable) { onResult(false, t.message) }
        })
    }

    // 5. Verifikasi Pengembalian
    fun verifyReturn(token: String, request: PengembalianRequest, onResult: (Boolean, String?) -> Unit) {
        apiService.submitPengembalian("Bearer $token", request).enqueue(object : Callback<PengembalianResponse> {
            override fun onResponse(call: Call<PengembalianResponse>, response: Response<PengembalianResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<PengembalianResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }

    // 6. Monitor Peminjaman Aktif (Filter status: dipinjam)
    fun getActiveLoans(token: String, onResult: (List<Peminjaman>?) -> Unit) {
        apiService.getAllPeminjaman("Bearer $token", "dipinjam").enqueue(object : Callback<PeminjamanListResponse> {
            override fun onResponse(call: Call<PeminjamanListResponse>, response: Response<PeminjamanListResponse>) {
                onResult(response.body()?.data)
            }
            override fun onFailure(call: Call<PeminjamanListResponse>, t: Throwable) { onResult(null) }
        })
    }

    // 7. Ambil Detail Pengembalian (Untuk melihat denda & catatan verifikasi)
    fun getReturnDetail(token: String, peminjamanId: Long, onResult: (Pengembalian?) -> Unit) {
        apiService.getPengembalianDetail("Bearer $token", peminjamanId).enqueue(object : Callback<PengembalianResponse> {
            override fun onResponse(call: Call<PengembalianResponse>, response: Response<PengembalianResponse>) {
                // Di sini Entity Pengembalian akhirnya digunakan secara eksplisit
                onResult(response.body()?.data)
            }
            override fun onFailure(call: Call<PengembalianResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }
}