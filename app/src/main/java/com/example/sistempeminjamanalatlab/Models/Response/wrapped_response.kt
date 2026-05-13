package com.example.sistempeminjamanalatlab.models.response

import com.google.gson.annotations.SerializedName

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
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("role") val role: String,
    @SerializedName("nama") val nama: String
)

// Alias siap pakai
typealias LoginResponse     = WrappedResponse<LoginData>
typealias BaseResponse      = WrappedResponse<Nothing?>