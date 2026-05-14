package com.example.sistempeminjamanalatlab.models.request

import com.google.gson.annotations.SerializedName

// File: PengembalianRequest.kt

data class PengembalianRequest(
    @SerializedName("peminjaman_id") val peminjamanId: Long,
    @SerializedName("catatan") val catatan: String? = null,

    // Opsional: Jika mahasiswa hanya mengembalikan seluruh isi peminjaman,
    // terkadang backend hanya butuh ID Peminjamannya saja.
    // Tapi jika per item, list di bawah ini diperlukan:
    @SerializedName("items_kembali") val itemsKembali: List<DetailKembaliRequest>? = null
)

data class DetailKembaliRequest(
    @SerializedName("detail_peminjaman_id") val detailId: Long,
    @SerializedName("kondisi_akhir") val kondisiAkhir: String,
    @SerializedName("catatan_pengembalian") val catatan: String? = null
)

// Tambahkan ini untuk endpoint /verify (oleh Laboran)
data class VerifyPengembalianRequest(
    @SerializedName("status_verifikasi") val statusVerifikasi: String, // "sesuai", "rusak", "hilang"
    @SerializedName("denda") val denda: Double = 0.0,
    @SerializedName("catatan") val catatan: String? = null
)