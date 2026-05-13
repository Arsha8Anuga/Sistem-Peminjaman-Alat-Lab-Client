package com.example.sistempeminjamanalatlab.models.response

import com.google.gson.annotations.SerializedName
import com.example.sistempeminjamanalatlab.models.entity.*

/**
 * Generic wrapper untuk semua response API.
 *
 * Contoh JSON dari server:
 * {
 *   "success": true,
 *   "message": "Data berhasil diambil",
 *   "data": { ... }
 * }
 *
 * Penggunaan:
 *   Call<WrappedResponse<User>>
 *   Call<WrappedResponse<List<Alat>>>
 *   Call<WrappedResponse<Peminjaman>>
 */
data class WrappedResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T? = null
)

// ─── AUTH ─────────────────────────────────────────────────

data class LoginData(
    @SerializedName("user_id") val userId: Long, // Tambahkan ini sebagai kunci FK
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("role") val role: String,
    @SerializedName("nama") val nama: String,
    @SerializedName("nim_nip") val nimNip: String? // Opsional: untuk ditampilkan di profil
)

// --- AUTH & USER ---
typealias LoginResponse     = WrappedResponse<LoginData>
typealias UserResponse      = WrappedResponse<User>
typealias UserListResponse  = WrappedResponse<List<User>>

// --- ALAT & KATEGORI ---
typealias AlatResponse      = WrappedResponse<Alat>
typealias AlatListResponse  = WrappedResponse<List<Alat>>
typealias KategoriResponse      = WrappedResponse<KategoriAlat>       // Untuk detail/create/update 1 kategori
typealias KategoriListResponse  = WrappedResponse<List<KategoriAlat>>

// --- PEMINJAMAN & TRANSAKSI ---
typealias PeminjamanResponse     = WrappedResponse<Peminjaman>
typealias PeminjamanListResponse = WrappedResponse<List<Peminjaman>>
typealias PengembalianResponse   = WrappedResponse<Pengembalian>
typealias KondisiLogListResponse = WrappedResponse<List<KondisiLog>>

// --- BASE (Untuk update/delete/create yang hanya butuh message) ---
typealias BaseResponse      = WrappedResponse<Nothing?>