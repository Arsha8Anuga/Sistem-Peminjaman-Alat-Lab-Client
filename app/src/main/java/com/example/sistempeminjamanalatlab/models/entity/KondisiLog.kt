package com.example.sistempeminjamanalatlab.models.entity

import com.google.gson.annotations.SerializedName

data class KondisiLog(
    @SerializedName("id") val id: Long,
    @SerializedName("alat_id") val alatId: Long,

    // Tambahkan ini: Untuk melacak log ini berasal dari transaksi mana
    @SerializedName("peminjaman_id") val peminjamanId: Long?,

    // Sesuai ENUM: baik, rusak_ringan, rusak_berat, maintenance, hilang
    @SerializedName("kondisi") val kondisi: String,

    @SerializedName("catatan") val catatan: String?,

    // Gunakan Long untuk ID atau User object untuk data lengkap
    @SerializedName("dicatat_oleh") val dicatatOleh: Long,

    @SerializedName("created_at") val createdAt: String,

    // Relasi ke User (Staff yang mencatat log)
    @SerializedName("pencatat") val pencatat: User?
)
