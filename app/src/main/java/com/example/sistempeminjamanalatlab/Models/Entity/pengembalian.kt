package com.example.sistempeminjamanalatlab.models.entity

import com.google.gson.annotations.SerializedName

data class Pengembalian(
    @SerializedName("id") val id: Long,
    @SerializedName("peminjaman_id") val peminjamanId: Long,
    @SerializedName("tanggal_dikembalikan") val tanggalDikembalikan: String,
    @SerializedName("denda") val denda: Double,
    @SerializedName("status_verifikasi") val statusVerifikasi: String,
    @SerializedName("catatan") val catatan: String?
)

data class KondisiLog(
    @SerializedName("id") val id: Long,
    @SerializedName("alat_id") val alatId: Long,
    @SerializedName("kondisi") val kondisi: String,
    @SerializedName("catatan") val catatan: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("pencatat") val pencatat: User?
)