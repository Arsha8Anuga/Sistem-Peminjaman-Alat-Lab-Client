package com.example.sistempeminjamanalatlab.models.entity

import com.google.gson.annotations.SerializedName

data class KondisiLog(
    @SerializedName("id") val id: Long,
    @SerializedName("alat_id") val alatId: Long,

    // Relasi ke transaksi (nullable karena bisa jadi pengecekan rutin)
    @SerializedName("peminjaman_id") val peminjamanId: Long?,

    // Status: baik, rusak_ringan, rusak_berat, maintenance, hilang
    @SerializedName("kondisi") val kondisi: String,

    @SerializedName("catatan") val catatan: String?,

    // URL Foto bukti kondisi alat (Penting untuk audit lab)
    @SerializedName("foto") val foto: String?,

    @SerializedName("dicatat_oleh") val dicatatOleh: Long,
    @SerializedName("created_at") val createdAt: String,

    // Relasi objek untuk menampilkan nama Laboran di UI
    @SerializedName("pencatat") val pencatat: User? = null
)
