package com.example.sistempeminjamanalatlab.models.response

import com.google.gson.annotations.SerializedName
import com.example.sistempeminjamanalatlab.models.entity.*

// File: WrappedResponse.kt

data class WrappedResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T? = null
)

// ─── AUTH DATA ─────────────────────────────────────────────
data class LoginData(
    @SerializedName("user_id") val userId: Long,
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("role") val role: String,
    @SerializedName("nama") val nama: String,
    @SerializedName("nim_nip") val nimNip: String?
)

// --- AUTH & USER ---
typealias LoginResponse     = WrappedResponse<LoginData>
typealias UserResponse      = WrappedResponse<User>
typealias UserListResponse  = WrappedResponse<List<User>>

// --- ALAT & KATEGORI ---
typealias AlatResponse      = WrappedResponse<Alat>
typealias AlatListResponse  = WrappedResponse<List<Alat>>
typealias KategoriResponse     = WrappedResponse<KategoriAlat>
typealias KategoriListResponse = WrappedResponse<List<KategoriAlat>>

// --- PEMINJAMAN & TRANSAKSI ---
typealias PeminjamanResponse     = WrappedResponse<Peminjaman>
typealias PeminjamanListResponse = WrappedResponse<List<Peminjaman>>
typealias PengembalianResponse   = WrappedResponse<Pengembalian>
typealias KondisiLogListResponse = WrappedResponse<List<KondisiLog>>

// --- BASE RESPONSE ---
// Menggunakan Any? jauh lebih aman untuk response yang data-nya null/kosong
typealias BaseResponse      = WrappedResponse<Any?>