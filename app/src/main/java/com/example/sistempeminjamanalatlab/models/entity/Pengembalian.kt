package com.example.sistempeminjamanalatlab.models.entity

import com.google.gson.annotations.SerializedName

data class Pengembalian(
    @SerializedName("id") val id: Long,
    @SerializedName("peminjaman_id") val peminjamanId: Long,

    // Staff yang memverifikasi pengembalian fisik alat
    @SerializedName("diterima_oleh") val diterimaOleh: Long?,

    @SerializedName("tanggal_dikembalikan") val tanggalDikembalikan: String,

    // Menggunakan Double untuk nilai uang denda (DECIMAL)
    @SerializedName("denda") val denda: Double,

    // Status: menunggu (pending), sesuai, rusak, hilang
    @SerializedName("status_verifikasi") val statusVerifikasi: String,

    @SerializedName("catatan") val catatan: String?,

    // Tambahkan ini: URL Foto bukti fisik saat pengembalian (Audit)
    @SerializedName("foto_bukti") val fotoBukti: String?,

    @SerializedName("created_at") val createdAt: String?,

    // Relasi objek untuk menampilkan nama Laboran di UI
    @SerializedName("staff_penerima") val staffPenerima: User? = null,

    // Relasi ke data peminjaman utama
    @SerializedName("peminjaman") val peminjaman: Peminjaman? = null
)