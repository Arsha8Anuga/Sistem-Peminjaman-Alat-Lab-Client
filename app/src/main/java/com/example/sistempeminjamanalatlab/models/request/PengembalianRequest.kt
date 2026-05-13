package com.example.sistempeminjamanalatlab.models.request

import com.google.gson.annotations.SerializedName

data class PengembalianRequest(
    @SerializedName("peminjaman_id") val peminjamanId: Long,
    @SerializedName("diterima_oleh") val diterimaOleh: Long, // ID Laboran yang menerima
    @SerializedName("tanggal_dikembalikan") val tanggalDikembalikan: String, // "yyyy-MM-dd HH:mm:ss"
    @SerializedName("denda") val denda: Double = 0.0,
    @SerializedName("status_verifikasi") val statusVerifikasi: String, // "sesuai", "rusak", "hilang"
    @SerializedName("catatan") val catatan: String? = null,
    @SerializedName("items_kembali") val itemsKembali: List<DetailKembaliRequest>
)

data class DetailKembaliRequest(
    @SerializedName("detail_peminjaman_id") val detailId: Long, // ID baris di detail_peminjaman
    @SerializedName("kondisi_akhir") val kondisiAkhir: String, // "baik", "rusak_ringan", "hilang"
    @SerializedName("catatan_pengembalian") val catatan: String? = null
)