package com.example.sistempeminjamanalatlab.models.entity

import com.google.gson.annotations.SerializedName

data class Pengembalian(
    @SerializedName("id") val id: Long,
    @SerializedName("peminjaman_id") val peminjamanId: Long,

    // Tambahkan ini: Untuk mencatat staff yang menerima pengembalian
    @SerializedName("diterima_oleh") val diterimaOleh: Long,

    @SerializedName("tanggal_dikembalikan") val tanggalDikembalikan: String,

    // Gunakan Double untuk mengakomodasi DECIMAL di database
    @SerializedName("denda") val denda: Double,

    // Sesuai ENUM: menunggu, sesuai, rusak, hilang
    @SerializedName("status_verifikasi") val statusVerifikasi: String,

    @SerializedName("catatan") val catatan: String?,
    @SerializedName("created_at") val createdAt: String?,

    // Relasi Opsional (jika API mengirimkan data User hasil JOIN)
    @SerializedName("staff_penerima") val staffPenerima: User? = null
)